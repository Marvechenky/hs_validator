package com.polaris.HS.Code.Validator.controller;

import com.polaris.HS.Code.Validator.data.dto.HsCodeSearchDto;
import com.polaris.HS.Code.Validator.data.dto.HsValidationResponse;
import com.polaris.HS.Code.Validator.service.HsCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hs")

public class HsCodeController {

    private final HsCodeService hsCodeService;

    @GetMapping("/validate")
    public ResponseEntity<HsValidationResponse> validate(
            @RequestParam String code,
            @RequestParam(required = false) String country){

        HsValidationResponse response = hsCodeService.validate(code, country);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HsCodeSearchDto>> search(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "20") int limit
    ) {
        if (query == null || query.trim().length() < 2) {
            return ResponseEntity.badRequest().build();
        }

        List<HsCodeSearchDto> responses = hsCodeService.search(query.trim(), limit);
        return ResponseEntity.ok(responses);
    }


}
