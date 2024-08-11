alter table transaction_history
    add column exchange_fee decimal(4,4) not null,
    add column amount decimal(40,10) not null;

