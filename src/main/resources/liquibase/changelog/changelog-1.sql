alter table account add column deleted bool not null default false;
alter table transactions add column deleted bool not null default false;
alter table users add column deleted bool not null default false;