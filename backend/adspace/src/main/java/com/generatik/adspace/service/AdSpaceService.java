package com.generatik.adspace.service;

import com.generatik.adspace.dto.adspace.AdSpaceResponseDto;

import java.util.List;

public interface AdSpaceService {

    /**
     * Get all available ad spaces with optional filtering by type and city.
     *
     * @param type Optional ad space type filter
     * @param city Optional city filter
     * @return List of available ad spaces
     */
    List<AdSpaceResponseDto> getAvailableAdSpaces(String type, String city);

    /**
     * Get ad space by ID.
     *
     * @param id Ad space ID
     * @return Ad space details
     * @throws com.generatik.adspace.exception.NotFoundException if not found
     */
    AdSpaceResponseDto getAdSpaceById(Long id);
}

