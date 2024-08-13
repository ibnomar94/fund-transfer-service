alter table transaction_history
    add column exchange_fee decimal(8, 4) not null;
alter table transaction_history
    add column amount decimal(40, 10) not null;
