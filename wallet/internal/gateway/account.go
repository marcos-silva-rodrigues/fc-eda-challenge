package gateway

import "github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/entity"

type AccountGateway interface {
	Save(account *entity.Account) error
	FindByID(id string) (*entity.Account, error)
	UpdateBalance(account *entity.Account) error
}
