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
CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER','TRANSFER_OUT','TRANSFER_IN','TRANSFER_EXTERN' )),
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(20)  CHECK (status IN ('COMPLETED', 'FAILED', 'PENDING')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);

CREATE TABLE IF NOT EXISTS fee_rules (
id SERIAL PRIMARY KEY,
operation_type VARCHAR(50)  NOT NULL,
fee_mode VARCHAR(10) NOT NULL CHECK (fee_mode IN ('FIXED', 'PERCENT')),
fee_value DECIMAL(10,2) NOT NULL,
currency VARCHAR(3) DEFAULT 'MAD',
is_active BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bank_fees (
id SERIAL PRIMARY KEY,
source_type VARCHAR(20) NOT NULL CHECK (source_type IN ('TRANSACTION', 'CREDIT')),
source_id INTEGER NOT NULL,
amount DECIMAL(15,2) NOT NULL,
fee_type VARCHAR(50) NOT NULL,
currency VARCHAR(3) DEFAULT 'MAD',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts_externe (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(20)  NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    bank_name VARCHAR(100) NOT NULL,
    holder_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
CREATE TABLE IF NOT EXISTS credits (
    id SERIAL PRIMARY KEY,
    amount DECIMAL(15,2),
    duration INTEGER,
    monthly_payment DECIMAL(15,2),
    status VARCHAR(20),
    remaining_amount DECIMAL(15,2),
    client_id BIGINT REFERENCES clients(id),
    request_date DATE,
    client_income DECIMAL(15,2)
);

ALTER TABLE accounts_externe ADD CONSTRAINT accounts_externe_account_number_key UNIQUE (account_number);
INSERT INTO accounts_externe (account_number, balance, bank_name, holder_name) VALUES
('BMCE-1234', 200.30, 'BMCE', 'Ahmed Alami'),
('CIH-1234',300.50 , 'CIH', 'Fatima Zahra')
ON CONFLICT (account_number) DO NOTHING;

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
INSERT INTO fee_rules (operation_type, fee_mode, fee_value, currency, is_active) VALUES
    ('TRANSFER_EXTERN', 'FIXED', 25.00, 'MAD', TRUE)
