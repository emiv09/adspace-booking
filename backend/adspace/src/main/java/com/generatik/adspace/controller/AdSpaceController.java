package com.generatik.adspace.controller;

import com.generatik.adspace.dto.adspace.AdSpaceResponseDto;
import com.generatik.adspace.service.AdSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ad-spaces")
@RequiredArgsConstructor
public class AdSpaceController {

    private final AdSpaceService adSpaceService;

    @GetMapping
    public ResponseEntity<List<AdSpaceResponseDto>> getAvailableAdSpaces(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city) {
        List<AdSpaceResponseDto> adSpaces = adSpaceService.getAvailableAdSpaces(type, city);
        return ResponseEntity.ok(adSpaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdSpaceResponseDto> getAdSpaceById(@PathVariable Long id) {
        AdSpaceResponseDto adSpace = adSpaceService.getAdSpaceById(id);
        return ResponseEntity.ok(adSpace);
    }
}

