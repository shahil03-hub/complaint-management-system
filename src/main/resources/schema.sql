CREATE TABLE IF NOT EXISTS complaints (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    subject     VARCHAR(200) NOT NULL,
    description TEXT         NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
