CREATE INDEX idx_national_hs6
    ON hs_national_extension (hs6_code);

CREATE INDEX idx_national_country
    ON hs_national_extension (country_code);


CREATE INDEX idx_national_description_trgm
    ON hs_national_extension
    USING GIN (description gin_trgm_ops);
