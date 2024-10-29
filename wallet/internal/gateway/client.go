package gateway

import "github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/entity"

type ClientGateway interface {
	Get(id string) (*entity.Client, error)
	Save(client *entity.Client) error
}
