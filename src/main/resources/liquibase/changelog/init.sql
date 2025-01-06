CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) not null,
    email VARCHAR(255) unique not null,
    active bool default true,
    created_at TIMESTAMP DEFAULT current_timestamp,
    created_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT current_timestamp,
    version int
    );