package gateway

import "github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/entity"

type TransactionGateway interface {
	Create(transaction *entity.Transaction) error
}
