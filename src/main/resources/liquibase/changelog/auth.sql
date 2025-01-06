create table user_login(
    id varchar(50) primary key,
    username varchar(100),
    password varchar(100),
    active bool default true,
    created_at TIMESTAMP DEFAULT current_timestamp,
    created_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT current_timestamp,
    version int
);

create table roles(
    id varchar(50) primary key,
    name varchar(100)
);

create table user_roles(
    user_id varchar(50) NOT NULL,
    role_id varchar(50) NOT NULL
);
