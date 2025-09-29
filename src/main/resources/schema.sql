CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('TELLER', 'MANAGER', 'ADMIN', 'AUDITOR')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    FullName VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    monthly_income DECIMAL(15,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL, -- Format: BK-YYYY-NNNN
    client_id INTEGER NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('CHECKING', 'SAVINGS', 'CREDIT')),
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(3) DEFAULT 'MAD',
    overdraft_limit DECIMAL(15,2) DEFAULT 0.00,
    interest_rate DECIMAL(5,4) DEFAULT 0.0000,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'SUSPENDED', 'CLOSED')),
    opened_date DATE DEFAULT CURRENT_DATE,
    closed_date DATE NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


INSERT INTO users (username, password, role)
VALUES
    ('admin', 'admin123', 'ADMIN'),
    ('teller1', 'teller123', 'TELLER'),
    ('manager1', 'manager123', 'MANAGER'),
    ('auditor1', 'auditor123', 'AUDITOR')
    ON CONFLICT (username) DO NOTHING;

INSERT INTO clients (FullName, email, phone, address, monthly_income)
VALUES
    ('Jean', 'jean.dupont@email.com', '0612345678', '123 Rue de Paris', 5000.00),
    ('Marie', 'marie.martin@email.com', '0623456789', '456 Avenue Lyon', 6500.00)
    ON CONFLICT (email) DO NOTHING;