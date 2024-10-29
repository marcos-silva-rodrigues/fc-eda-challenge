package main

import (
	"context"
	"database/sql"
	"fmt"

	ckafka "github.com/confluentinc/confluent-kafka-go/kafka"
	_ "github.com/go-sql-driver/mysql"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/database"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/event"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/event/handler"
	createaccount "github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/usecase/create_account"
	createclient "github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/usecase/create_client"
	createtransaction "github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/usecase/create_transaction"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/web"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/web/webserver"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/pkg/events"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/pkg/kafka"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/pkg/uow"
)

func main() {
	db, err := sql.Open("mysql", fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8&parseTime=True&loc=Local", "root", "root", "wallet_db", "3306", "wallet"))
	if err != nil {
		panic(err)
	}

	defer db.Close()

	configMap := ckafka.ConfigMap{
		"bootstrap.servers": "kafka:29092",
		"group.id":          "wallet",
	}

	kafkaProducer := kafka.NewKafkaProducer(&configMap)
	transactionCreatedHandler := handler.NewTransactionCreatedKakfaHandler(kafkaProducer)
	balanceUpdatedHandler := handler.NewBalanceUpdatedKakfaHandler((kafkaProducer))

	eventDispatcher := events.NewEventDispatcher()
	eventDispatcher.Register("TransactionCreated", transactionCreatedHandler)
	eventDispatcher.Register("BalanceUpdated", balanceUpdatedHandler)

	clientDB := database.NewClientDB(db)
	accountDB := database.NewAccountDB(db)

	ctx := context.Background()
	uow := uow.NewUow(ctx, db)

	uow.Register("ClientGateway", func(tx *sql.Tx) interface{} {
		return database.NewClientDB(db)
	})

	uow.Register("AccountGateway", func(tx *sql.Tx) interface{} {
		return database.NewAccountDB(db)
	})

	uow.Register("TransactionGateway", func(tx *sql.Tx) interface{} {
		return database.NewTransactionDB(db)
	})

	createClientUseCase := createclient.NewCreateClientUseCase(clientDB)
	createAccountUseCase := createaccount.NewCreateAccountUseCase(accountDB, clientDB)

	transactionEventCreated := event.NewTransactionCreated()
	balanceUpdatedEvent := event.NewBalanceUpdated()
	createTransactionUseCase := createtransaction.NewCreateTransactionUseCase(
		eventDispatcher,
		transactionEventCreated,
		balanceUpdatedEvent,
		uow,
	)

	webserver := webserver.NewWebServer(":8080")

	clientHandler := web.NewWebClientHandler(*createClientUseCase)
	accountHandler := web.NewWebAccountHandler(*createAccountUseCase)
	transactionHandler := web.NewWebTransactionHandler(*createTransactionUseCase)

	webserver.AddHandler("/clients", clientHandler.CreateClient)
	webserver.AddHandler("/accounts", accountHandler.CreateAccount)
	webserver.AddHandler("/transactions", transactionHandler.CreateTransaction)

	webserver.Start()
}
