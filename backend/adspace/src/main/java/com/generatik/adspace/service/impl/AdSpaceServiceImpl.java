package com.generatik.adspace.service.impl;

import com.generatik.adspace.dto.adspace.AdSpaceResponseDto;
import com.generatik.adspace.entity.AdSpace;
import com.generatik.adspace.entity.AdSpaceStatus;
import com.generatik.adspace.entity.AdSpaceType;
import com.generatik.adspace.exception.NotFoundException;
import com.generatik.adspace.repository.AdSpaceRepository;
import com.generatik.adspace.service.AdSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdSpaceServiceImpl implements AdSpaceService {

    private final AdSpaceRepository adSpaceRepository;

    @Override
    public List<AdSpaceResponseDto> getAvailableAdSpaces(String type, String city) {
        List<AdSpace> adSpaces;

        if (type != null && city != null) {
            AdSpaceType adSpaceType = parseAdSpaceType(type);
            adSpaces = adSpaceRepository.findByStatusAndTypeAndCity(
                    AdSpaceStatus.AVAILABLE, adSpaceType, city);
        } else if (type != null) {
            AdSpaceType adSpaceType = parseAdSpaceType(type);
            adSpaces = adSpaceRepository.findByStatusAndType(
                    AdSpaceStatus.AVAILABLE, adSpaceType);
        } else if (city != null) {
            adSpaces = adSpaceRepository.findByStatusAndCity(
                    AdSpaceStatus.AVAILABLE, city);
        } else {
            adSpaces = adSpaceRepository.findByStatus(AdSpaceStatus.AVAILABLE);
        }

        return adSpaces.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdSpaceResponseDto getAdSpaceById(Long id) {
        AdSpace adSpace = adSpaceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad space not found with id: " + id));
        return mapToDto(adSpace);
    }

    private AdSpaceType parseAdSpaceType(String type) {
        try {
            return AdSpaceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Invalid ad space type: " + type);
        }
    }

    private AdSpaceResponseDto mapToDto(AdSpace adSpace) {
        return AdSpaceResponseDto.builder()
                .id(adSpace.getId())
                .name(adSpace.getName())
                .type(adSpace.getType())
                .city(adSpace.getCity())
                .address(adSpace.getAddress())
                .pricePerDay(adSpace.getPricePerDay())
                .status(adSpace.getStatus())
                .build();
    }
}

