CREATE INDEX idx_national_hs6
    ON hs_national_extension (hs6_code);

CREATE INDEX idx_national_country
    ON hs_national_extension (country_code);

CREATE INDEX idx_national_desc_trgm
    ON hs_national_extension
    USING gin (description gin_trgm_ops);
