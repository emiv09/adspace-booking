package com.generatik.adspace.controller;

import com.generatik.adspace.dto.booking.BookingResponseDto;
import com.generatik.adspace.dto.booking.CreateBookingRequestDto;
import com.generatik.adspace.entity.BookingStatus;
import com.generatik.adspace.service.BookingRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booking-requests")
@RequiredArgsConstructor
public class BookingRequestController {

    private final BookingRequestService bookingRequestService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody CreateBookingRequestDto request) {
        BookingResponseDto booking = bookingRequestService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        BookingResponseDto booking = bookingRequestService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(
            @RequestParam(required = false) BookingStatus status) {
        List<BookingResponseDto> bookings = bookingRequestService.getAllBookings(Optional.ofNullable(status));
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<BookingResponseDto> approveBooking(@PathVariable Long id) {
        BookingResponseDto booking = bookingRequestService.approveBooking(id);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<BookingResponseDto> rejectBooking(@PathVariable Long id) {
        BookingResponseDto booking = bookingRequestService.rejectBooking(id);
        return ResponseEntity.ok(booking);
    }
}

