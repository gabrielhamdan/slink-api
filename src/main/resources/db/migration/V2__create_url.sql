CREATE SEQUENCE url_seq START 1;

CREATE TABLE url
(
    id               BIGINT PRIMARY KEY DEFAULT nextval('url_seq'),
    original_url     TEXT        NOT NULL,
    slink            VARCHAR(16) NOT NULL UNIQUE,
    expires_at       TIMESTAMP,
    click_count      INTEGER,
    last_accessed_at TIMESTAMP,
    user_id          BIGINT REFERENCES api_user (id)
);