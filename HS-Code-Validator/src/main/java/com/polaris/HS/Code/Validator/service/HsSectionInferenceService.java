package com.polaris.HS.Code.Validator.service;

import com.polaris.HS.Code.Validator.repository.HsSectionInferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HsSectionInferenceService {

    private final HsSectionInferenceRepository hsSectionInferenceRepository;

    public String detectSection(String query){

        List<Object[]> sections = hsSectionInferenceRepository.detectSection(query);

        if (sections.isEmpty()) {
            return null;
        }

        if (sections.size() == 1){
            return (String) sections.get(0)[0];
        }

        double top = ((Number) sections.get(0)[1]).doubleValue();
        double second = ((Number) sections.get(1)[1]).doubleValue();

        if (top - second < 0.01){
            return null;
        }

        return (String) sections.get(0)[0];
    }
}
