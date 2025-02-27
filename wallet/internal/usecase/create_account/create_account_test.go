package createaccount

import (
	"testing"

	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/entity"
	"github.com/marcos-silva-rodrigues/FC-EDA-WalletCore/internal/usecase/mocks"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

func TestCreateAccountUseCaseExecute(t *testing.T) {
	client, _ := entity.NewClient("john dow", "j@j")
	clientMock := &mocks.ClientGatewayMock{}
	clientMock.On("Get", client.ID).Return(client, nil)

	accountMock := &mocks.AccountGatewayMock{}
	accountMock.On("Save", mock.Anything).Return(nil)

	uc := NewCreateAccountUseCase(accountMock, clientMock)

	inputDto := CreateAccountInputDTO{
		ClientID: client.ID,
	}

	output, err := uc.Execute(inputDto)

	assert.Nil(t, err)
	assert.NotNil(t, output.ID)
	clientMock.AssertExpectations(t)
	accountMock.AssertExpectations(t)
	clientMock.AssertNumberOfCalls(t, "Get", 1)
	accountMock.AssertNumberOfCalls(t, "Save", 1)
}
