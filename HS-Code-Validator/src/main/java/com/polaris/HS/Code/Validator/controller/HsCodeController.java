package com.polaris.HS.Code.Validator.controller;

import com.polaris.HS.Code.Validator.data.dto.HsCodeSearchDto;
import com.polaris.HS.Code.Validator.data.dto.HsValidationResponse;
import com.polaris.HS.Code.Validator.service.HsCodeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hs")

public class HsCodeController {

    private final HsCodeService hsCodeService;

    @GetMapping("/validate")
    @Operation(summary = "search by hs code")
    public ResponseEntity<HsValidationResponse> validate(
            @RequestParam String code,
            @RequestParam(required = false) String country){

        HsValidationResponse response = hsCodeService.validate(code, country);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "search by product description")
    public ResponseEntity<List<HsCodeSearchDto>> search(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (query.trim().length() < 2) {
            return ResponseEntity.badRequest().build();
        }

        List<HsCodeSearchDto> responses = hsCodeService.search(query, limit);
        return ResponseEntity.ok(responses);
    }

}
