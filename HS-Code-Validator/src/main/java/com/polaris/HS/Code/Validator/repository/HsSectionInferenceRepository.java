package com.polaris.HS.Code.Validator.repository;

import com.polaris.HS.Code.Validator.data.model.HsSectionInference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HsSectionInferenceRepository extends JpaRepository<HsSectionInference, Long> {

    @Query(value = """
        SELECT section_code,
                ts_rank(section_vector, websearch_to_tsquery('english_hs', :query)) AS rank
        FROM hs_section_inference
        WHERE section_vector @@ websearch_to_tsquery('english_hs', :query)
        ORDER BY rank DESC
        LIMIT 2
        """, nativeQuery = true)
    List<Object[]> detectSection(@Param("query") String query);
}
