CREATE TABLE IF NOT EXISTS balance.balances (
    id varchar(255),
    balance float,
    updated_at date,

    primary key(id)
);