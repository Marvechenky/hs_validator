CREATE INDEX idx_hs_code_code
    ON hs_code (code);

CREATE INDEX idx_hs_code_level
    ON hs_code (level);

CREATE INDEX idx_hs_code_parent
    ON hs_code (parent_code);

CREATE INDEX idx_hs_code_description_fts
    ON hs_code
    USING GIN (to_tsvector('english', description));
