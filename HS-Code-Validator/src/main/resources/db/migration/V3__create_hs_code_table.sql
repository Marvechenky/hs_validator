CREATE TABLE hs_code (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(10) NOT NULL,
    level INTEGER NOT NULL,

    description TEXT NOT NULL,

    parent_code VARCHAR(10),
    section VARCHAR(5) NOT NULL,

    search_vector tsvector GENERATED ALWAYS AS(
        to_tsvector('english_hs', description)
    ) STORED,

    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_hs_code UNIQUE (code)
);


