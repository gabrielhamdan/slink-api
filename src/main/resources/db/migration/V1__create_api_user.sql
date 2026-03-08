CREATE TABLE api_user
(
    id        BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) UNIQUE,
    api_key   VARCHAR(255) UNIQUE
);