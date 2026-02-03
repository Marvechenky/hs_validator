CREATE TABLE hs_national_extension (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(10) NOT NULL,
    hs6_code VARCHAR(6) NOT NULL,
    country_code VARCHAR(2) NOT NULL,

    description TEXT NOT NULL,
    level INTEGER NOT NULL CHECK (level IN (8, 10)),

    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_national_code UNIQUE (code, country_code),
    CONSTRAINT fk_hs6
        FOREIGN KEY (hs6_code)
        REFERENCES hs_code(code)
        ON DELETE CASCADE
);
