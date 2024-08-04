CREATE TABLE IF NOT EXISTS account_holder (
  id int8 not null AUTO_INCREMENT,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  ssn varchar(255) not null,
  main_address varchar(255),
  primary key (id)
  );

CREATE INDEX user_ssn_idx on account_holder (ssn);
