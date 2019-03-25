CREATE TABLE account (
  owner varchar(255) not null,
  iban varchar(255) primary key,
  currency varchar(255) not null,
  saldo integer not null
);

CREATE TABLE transaction (
  id uuid primary key,
  debtor_owner varchar(255) not null,
  debtor_iban varchar(255) not null,
  creditor_owner varchar(255) not null,
  creditor_iban varchar(255) not null,
  bookingdate date not null,
  amount integer not null,
  currency varchar(255) not null,
  account varchar(255) not null,
  foreign key(account) references account(iban)
);
