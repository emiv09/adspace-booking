package com.generatik.adspace.dto.booking;

import com.generatik.adspace.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private Long id;
    private Long adSpaceId;
    private String adSpaceName;
    private String advertiserName;
    private String advertiserEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private BigDecimal totalCost;
    private LocalDateTime createdAt;
}

