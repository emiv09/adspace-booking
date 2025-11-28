package com.generatik.adspace.service.impl;

import com.generatik.adspace.dto.booking.BookingResponseDto;
import com.generatik.adspace.dto.booking.CreateBookingRequestDto;
import com.generatik.adspace.entity.AdSpace;
import com.generatik.adspace.entity.AdSpaceStatus;
import com.generatik.adspace.entity.BookingRequest;
import com.generatik.adspace.entity.BookingStatus;
import com.generatik.adspace.exception.BusinessValidationException;
import com.generatik.adspace.exception.NotFoundException;
import com.generatik.adspace.repository.AdSpaceRepository;
import com.generatik.adspace.repository.BookingRequestRepository;
import com.generatik.adspace.service.BookingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingRequestServiceImpl implements BookingRequestService {

    private final BookingRequestRepository bookingRequestRepository;
    private final AdSpaceRepository adSpaceRepository;

    private static final int MINIMUM_BOOKING_DAYS = 7;

    @Override
    public BookingResponseDto createBooking(CreateBookingRequestDto request) {
        // Validate ad space exists
        AdSpace adSpace = adSpaceRepository.findById(request.getAdSpaceId())
                .orElseThrow(() -> new NotFoundException("Ad space not found with id: " + request.getAdSpaceId()));

        // Validate dates
        validateBookingDates(request.getStartDate(), request.getEndDate());

        // Validate ad space is available
        if (adSpace.getStatus() != AdSpaceStatus.AVAILABLE) {
            throw new BusinessValidationException("Ad space is not available for booking. Current status: " + adSpace.getStatus());
        }

        // Check for overlapping approved bookings
        List<BookingRequest> overlappingBookings = bookingRequestRepository.findOverlappingBookings(
                adSpace.getId(),
                BookingStatus.APPROVED,
                request.getStartDate(),
                request.getEndDate()
        );

        if (!overlappingBookings.isEmpty()) {
            throw new BusinessValidationException("Ad space already has approved bookings for the selected date range");
        }

        // Calculate total cost
        long numberOfDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal totalCost = adSpace.getPricePerDay().multiply(BigDecimal.valueOf(numberOfDays));

        // Create booking
        BookingRequest booking = BookingRequest.builder()
                .adSpace(adSpace)
                .advertiserName(request.getAdvertiserName())
                .advertiserEmail(request.getAdvertiserEmail())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(BookingStatus.PENDING)
                .totalCost(totalCost)
                .build();

        BookingRequest savedBooking = bookingRequestRepository.save(booking);
        return mapToDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long id) {
        BookingRequest booking = bookingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + id));
        return mapToDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings(Optional<BookingStatus> status) {
        List<BookingRequest> bookings;

        if (status.isPresent()) {
            bookings = bookingRequestRepository.findByStatus(status.get());
        } else {
            bookings = bookingRequestRepository.findAll();
        }

        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto approveBooking(Long id) {
        BookingRequest booking = bookingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BusinessValidationException("Only PENDING bookings can be approved. Current status: " + booking.getStatus());
        }

        // Update booking status
        booking.setStatus(BookingStatus.APPROVED);

        // Update ad space status to BOOKED
        AdSpace adSpace = booking.getAdSpace();
        adSpace.setStatus(AdSpaceStatus.BOOKED);
        adSpaceRepository.save(adSpace);

        BookingRequest updatedBooking = bookingRequestRepository.save(booking);
        return mapToDto(updatedBooking);
    }

    @Override
    public BookingResponseDto rejectBooking(Long id) {
        BookingRequest booking = bookingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BusinessValidationException("Only PENDING bookings can be rejected. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.REJECTED);
        BookingRequest updatedBooking = bookingRequestRepository.save(booking);
        return mapToDto(updatedBooking);
    }

    private void validateBookingDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        // Start date must be in the future
        if (!startDate.isAfter(today)) {
            throw new BusinessValidationException("Start date must be in the future");
        }

        // End date must be after start date
        if (!endDate.isAfter(startDate)) {
            throw new BusinessValidationException("End date must be after start date");
        }

        // Minimum booking duration is 7 days
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (daysBetween < MINIMUM_BOOKING_DAYS) {
            throw new BusinessValidationException("Minimum booking duration is " + MINIMUM_BOOKING_DAYS + " days");
        }
    }

    private BookingResponseDto mapToDto(BookingRequest booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .adSpaceId(booking.getAdSpace().getId())
                .adSpaceName(booking.getAdSpace().getName())
                .advertiserName(booking.getAdvertiserName())
                .advertiserEmail(booking.getAdvertiserEmail())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .totalCost(booking.getTotalCost())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}

