insert into roles (id, name) VALUES ('ROLE_USER', 'ROLE_USER');
insert into roles (id, name) VALUES ('ROLE_ADMIN', 'ROLE_ADMIN');

insert into user_login (id, username, password, version) VALUES ('user-admin001', 'admin001', '$2a$10$m8mim/uCJeAo5ooGIqw2YuD.ORGudUN7mI7WH7R/wRiP4wLY6N.bu', 0);

insert into user_roles(user_id,role_id) VALUES('user-admin001','ROLE_ADMIN');

insert into user_login (id, username, password, version) VALUES ('user-001', 'user001', '$2a$10$m8mim/uCJeAo5ooGIqw2YuD.ORGudUN7mI7WH7R/wRiP4wLY6N.bu', 0);

insert into user_roles(user_id,role_id) VALUES('user-001','ROLE_USER');