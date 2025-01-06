CREATE TABLE account (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) not null,
    balance decimal(16,2) default 0,
    created_at TIMESTAMP DEFAULT current_timestamp,
    created_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT current_timestamp,
    version int
    );

CREATE TABLE transactions (
    id VARCHAR(50) PRIMARY KEY,
    account_id VARCHAR(50) not null,
    type VARCHAR(10) not null,
    amount decimal(16,2) default 0,
    created_at TIMESTAMP DEFAULT current_timestamp,
    created_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT current_timestamp,
    version int
);
