package com.polaris.HS.Code.Validator.repository;

import com.polaris.HS.Code.Validator.data.model.HsCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HsCodeRepository extends JpaRepository<HsCode, Long> {

    boolean existsByCode(String code);

    long count();

    Optional<HsCode> findByCode(String Code);

//    @Query(
//            value = """
//            SELECT *,
//                   similarity(description, :query) AS score
//            FROM hs_code
//            WHERE similarity(description, :query) >= :minScore
//            ORDER BY score DESC, level ASC
//        """,
//            countQuery = """
//            SELECT count(*)
//            FROM hs_code
//            WHERE similarity(description, :query) >= :minScore
//        """,
//            nativeQuery = true
//    )

//@Query(
//        value = """
//        SELECT
//            code,
//            level,
//            description,
//            parent_code AS parentCode,
//            section,
//            similarity(description, :query) AS score
//        FROM hs_code
//        WHERE
//            LOWER(description) LIKE LOWER(CONCAT('%', :query, '%'))
//            OR similarity(description, :query) >= :minScore
//        ORDER BY
//            score DESC,
//            LENGTH(code) DESC,
//            level DESC
//        """,
//        countQuery = """
//        SELECT count(*)
//        FROM hs_code
//        WHERE
//            LOWER(description) LIKE LOWER(CONCAT('%', :query, '%'))
//            OR similarity(description, :query) >= :minScore
//        """,
//        nativeQuery = true
//)
//    Page<HsCode> searchByDescription(
//            @Param("query") String query,
//            @Param("minScore") float minScore,
//            Pageable pageable);


    @Query(
            value = """
    SELECT
        code,
        description,
        level,
        parent_code,
        section,
        similarity(description, :query) AS score
    FROM hs_code
    WHERE similarity(description, :query) >= :minScore
    ORDER BY score DESC, level ASC
    LIMIT :limit
    """,
            nativeQuery = true
    )
    List<Object[]> search(
            @Param("query") String query,
            @Param("minScore") double minScore,
            @Param("limit") int limit
    );



}
