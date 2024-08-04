CREATE TABLE IF NOT EXISTS account (
    id int8 not null AUTO_INCREMENT,
    account_holder_id int8 not null,
    balance decimal(40,10) not null,
    currency varchar(255) not null,
    primary key (id),
    CONSTRAINT fk_account_account_holder_id FOREIGN KEY (account_holder_id)
    REFERENCES account_holder(id)
    );

CREATE INDEX fk_account_account_holder_id on account (account_holder_id);
