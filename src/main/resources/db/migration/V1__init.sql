CREATE TABLE accounts (
  owner varchar(255) not null,
  iban varchar(255) primary key,
  currency varchar(255) not null,
  saldo integer not null
);

CREATE TABLE transactions (
  id uuid primary key,
  debtor_name varchar(255) not null,
  debtor_iban varchar(255) not null,
  creditor_name varchar(255) not null,
  creditor_iban varchar(255) not null,
  booking_date date not null,
  amount integer not null,
  currency varchar(255) not null,
  account varchar(255) not null,
  foreign key(account) references accounts(iban)
);
