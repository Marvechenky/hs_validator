CREATE TABLE hs_section_inference (
    section_code VARCHAR(5) PRIMARY KEY,

    section_vector tsvector NOT NULL
);