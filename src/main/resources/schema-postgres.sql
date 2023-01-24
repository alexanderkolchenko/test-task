CREATE TABLE if not exists banks
(
    id           UUID PRIMARY KEY not null,
    name_of_bank varchar(255) DEFAULT NULL
);

create table if not exists creditoffers
(
    id            UUID PRIMARY KEY not null,
    credit_amount numeric          not null,
    bank_id       UUID default null,
    credit_id     UUID default null,
    customer_id   UUID default null
);

CREATE TABLE if not exists customers
(
    id              UUID PRIMARY KEY not null,
    name            varchar(255) DEFAULT NULL,
    surname         varchar(255) DEFAULT NULL,
    patronymic      varchar(255) DEFAULT NULL,
    email           varchar(255) DEFAULT NULL,
    passport_number varchar(255) DEFAULT NULL,
    phone_number    varchar(255) DEFAULT NULL
);

create table if not exists credits
(
    id            UUID PRIMARY KEY not null,
    credit_limit  int              not null,
    interest_rate float            not null
);

create table if not exists credit_banks
(
    credit_id UUID not null,
    bank_id   UUID not null,
    primary key (credit_id, bank_id)
);

create table if not exists credit_payments
(
    id                            UUID PRIMARY KEY not null,
    date_of_payment               date default null,
    payment_of_month              float            not null,
    payment_of_percent_in_month   float            not null,
    payment_of_loan_body_in_month float            not null,
    credit_offer_id               UUID             not null
);

create table if not exists customer_banks
(
    customer_id UUID not null,
    bank_id     UUID not null,
    primary key (customer_id, bank_id)
);

alter table creditoffers
    add constraint co_id__customer_id foreign key (customer_id) references customers (id);
alter table creditoffers
    add constraint co_id__bank_id foreign key (bank_id) references banks (id);
alter table creditoffers
    add constraint co_id__credit_id foreign key (credit_id) references credits (id);


alter table credit_banks
    add constraint cb_id__bank_id foreign key (bank_id) references banks (id);
alter table credit_banks
    add constraint cb_id__credit_id foreign key (credit_id) references credits (id);


alter table credit_payments
    add constraint cp_id__CREDIT_OFFER_ID foreign key (credit_offer_id) references creditoffers (id);


alter table customer_banks
    add constraint cb_id__customer_id foreign key (customer_id) references customers (id);
alter table customer_banks
    add constraint cb_id__co_id foreign key (bank_id) references banks (id);