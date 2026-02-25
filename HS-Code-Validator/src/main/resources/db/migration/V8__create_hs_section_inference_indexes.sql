CREATE INDEX idx_hs_code_vector
ON hs_code USING GIN (search_vector);

CREATE INDEX idx_section_vector
ON hs_section_inference USING GIN (section_vector);