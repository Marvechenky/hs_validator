CREATE TEXT SEARCH CONFIGURATION english_hs ( COPY = english );

ALTER TEXT SEARCH CONFIGURATION english_hs
ALTER MAPPING FOR hword, hword_part, word
WITH unaccent, english_stem;