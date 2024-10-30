CREATE TABLE IF NOT EXISTS balance.balances (
    id varchar(255),
    balance float,
    updatedAt date,
    last_transaction varchar(255),

    primary key(id)
);