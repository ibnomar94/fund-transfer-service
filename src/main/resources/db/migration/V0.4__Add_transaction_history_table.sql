CREATE TABLE IF NOT EXISTS transaction_history (
    id int8 not null AUTO_INCREMENT,
    transaction_id varchar(255) not null,
    credited_id int8 not null,
    debited_id int8 not null,
    exchange_rate decimal(40,10) not null,
    primary key (id),
    CONSTRAINT fk_transaction_history_credited_id FOREIGN KEY (credited_id) REFERENCES account(id),
    CONSTRAINT fk_transaction_history_debited_id FOREIGN KEY (debited_id) REFERENCES account(id)
    );

CREATE INDEX fk_transaction_history_credited_id on transaction_history (credited_id);
CREATE INDEX fk_transaction_history_debited_id on transaction_history (debited_id);
