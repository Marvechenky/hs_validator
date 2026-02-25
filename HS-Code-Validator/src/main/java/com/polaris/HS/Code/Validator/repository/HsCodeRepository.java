package com.polaris.HS.Code.Validator.repository;

import com.polaris.HS.Code.Validator.data.model.HsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HsCodeRepository extends JpaRepository<HsCode, Long> {

//    boolean existsByCode(String code);

//    long count();

    Optional<HsCode> findByCode(String Code);


    @Query(value = """
    SELECT
        code,
        description,
        level,
        parent_code,
        section,

        (
            ts_rank_cd(
                search_vector,
                websearch_to_tsquery('english_hs', :query)
            ) * 0.6

            +

            CASE
                WHEN description ILIKE '%' || :query || '%' THEN 0.5
                ELSE 0
            END

            +

            CASE
                WHEN position(lower(:query) IN lower(description)) BETWEEN 1 AND 20 THEN 0.4
                WHEN position(lower(:query) IN lower(description)) BETWEEN 21 AND 50 THEN 0.2
                ELSE 0
            END

            -

            CASE
                WHEN description ~* ('\\y' || :query || '-\\w+') THEN 0.4
                ELSE 0
            END

            +

            CASE
                WHEN section = :section THEN 0.8
                ELSE 0
            END

        )
        *
        CASE
            WHEN level = 6 THEN 1.5
            WHEN level = 4 THEN 1.0
            WHEN level = 2 THEN 0.6
            ELSE 1.0
        END
        AS rank

    FROM hs_code
    WHERE search_vector @@ websearch_to_tsquery('english_hs', :query)
    ORDER BY rank DESC
    LIMIT :limit
""", nativeQuery = true)
    List<Object[]> searchWithSectionBoost(
            @Param("query") String query,
            @Param("section") String section,
            @Param("limit") int limit);
}
