package com.generatik.adspace.dto.adspace;

import com.generatik.adspace.entity.AdSpaceStatus;
import com.generatik.adspace.entity.AdSpaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdSpaceResponseDto {
    private Long id;
    private String name;
    private AdSpaceType type;
    private String city;
    private String address;
    private BigDecimal pricePerDay;
    private AdSpaceStatus status;
}

