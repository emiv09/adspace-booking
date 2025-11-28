package com.generatik.adspace.dto.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequestDto {

    @NotNull(message = "Ad space ID is required")
    private Long adSpaceId;

    @NotBlank(message = "Advertiser name is required")
    private String advertiserName;

    @NotBlank(message = "Advertiser email is required")
    @Email(message = "Invalid email format")
    private String advertiserEmail;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}

