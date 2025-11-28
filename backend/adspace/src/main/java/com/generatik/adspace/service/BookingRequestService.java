package com.generatik.adspace.service;

import com.generatik.adspace.dto.booking.BookingResponseDto;
import com.generatik.adspace.dto.booking.CreateBookingRequestDto;
import com.generatik.adspace.entity.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRequestService {

    /**
     * Create a new booking request.
     * Validates all business rules:
     * - Start date must be in the future
     * - End date must be after start date
     * - Minimum booking duration is 7 days
     * - Ad space must be AVAILABLE
     * - No overlapping APPROVED bookings
     *
     * @param request Booking request details
     * @return Created booking
     * @throws com.generatik.adspace.exception.NotFoundException if ad space not found
     * @throws com.generatik.adspace.exception.BusinessValidationException if validation fails
     */
    BookingResponseDto createBooking(CreateBookingRequestDto request);

    /**
     * Get booking by ID.
     *
     * @param id Booking ID
     * @return Booking details
     * @throws com.generatik.adspace.exception.NotFoundException if not found
     */
    BookingResponseDto getBookingById(Long id);

    /**
     * Get all bookings with optional status filter.
     *
     * @param status Optional booking status filter
     * @return List of bookings
     */
    List<BookingResponseDto> getAllBookings(Optional<BookingStatus> status);

    /**
     * Approve a pending booking.
     * Only PENDING bookings can be approved.
     * Sets the ad space status to BOOKED.
     *
     * @param id Booking ID
     * @return Updated booking
     * @throws com.generatik.adspace.exception.NotFoundException if not found
     * @throws com.generatik.adspace.exception.BusinessValidationException if not in PENDING status
     */
    BookingResponseDto approveBooking(Long id);

    /**
     * Reject a pending booking.
     * Only PENDING bookings can be rejected.
     *
     * @param id Booking ID
     * @return Updated booking
     * @throws com.generatik.adspace.exception.NotFoundException if not found
     * @throws com.generatik.adspace.exception.BusinessValidationException if not in PENDING status
     */
    BookingResponseDto rejectBooking(Long id);
}

