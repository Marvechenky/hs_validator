INSERT INTO hs_section_inference (section_code, section_vector)
SELECT
    section,
    to_tsvector('english_hs', string_agg(description, ' '))
FROM hs_code
WHERE section IS NOT NULL
    AND section <> 'TOTAL'
GROUP BY section;