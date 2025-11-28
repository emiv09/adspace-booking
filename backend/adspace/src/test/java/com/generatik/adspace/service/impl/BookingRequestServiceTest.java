package com.generatik.adspace.service.impl;

import com.generatik.adspace.dto.booking.BookingResponseDto;
import com.generatik.adspace.dto.booking.CreateBookingRequestDto;
import com.generatik.adspace.entity.AdSpace;
import com.generatik.adspace.entity.AdSpaceStatus;
import com.generatik.adspace.entity.AdSpaceType;
import com.generatik.adspace.entity.BookingRequest;
import com.generatik.adspace.entity.BookingStatus;
import com.generatik.adspace.exception.BusinessValidationException;
import com.generatik.adspace.exception.NotFoundException;
import com.generatik.adspace.repository.AdSpaceRepository;
import com.generatik.adspace.repository.BookingRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRequestServiceTest {

    @Mock
    private BookingRequestRepository bookingRequestRepository;

    @Mock
    private AdSpaceRepository adSpaceRepository;

    @InjectMocks
    private BookingRequestServiceImpl bookingRequestService;

    private AdSpace testAdSpace;
    private CreateBookingRequestDto validBookingRequest;

    @BeforeEach
    void setUp() {
        testAdSpace = AdSpace.builder()
                .id(1L)
                .name("Billboard Downtown")
                .type(AdSpaceType.BILLBOARD)
                .city("New York")
                .address("123 Main St")
                .pricePerDay(BigDecimal.valueOf(100))
                .status(AdSpaceStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .bookingRequests(new ArrayList<>())
                .build();

        validBookingRequest = CreateBookingRequestDto.builder()
                .adSpaceId(1L)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .build();
    }

    @Test
    @DisplayName("Should create booking successfully when all validations pass")
    void createBooking_Success() {
        // Arrange
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(testAdSpace));
        when(bookingRequestRepository.findOverlappingBookings(
                eq(1L), eq(BookingStatus.APPROVED), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        BookingRequest savedBooking = BookingRequest.builder()
                .id(1L)
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(validBookingRequest.getStartDate())
                .endDate(validBookingRequest.getEndDate())
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .createdAt(LocalDateTime.now())
                .build();

        when(bookingRequestRepository.save(any(BookingRequest.class))).thenReturn(savedBooking);

        // Act
        BookingResponseDto result = bookingRequestService.createBooking(validBookingRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(result.getAdvertiserName()).isEqualTo("John Doe");
        assertThat(result.getTotalCost()).isEqualTo(BigDecimal.valueOf(800));

        verify(adSpaceRepository).findById(1L);
        verify(bookingRequestRepository).findOverlappingBookings(eq(1L), eq(BookingStatus.APPROVED), any(), any());
        verify(bookingRequestRepository).save(any(BookingRequest.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when ad space does not exist")
    void createBooking_AdSpaceNotFound() {
        // Arrange
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.createBooking(validBookingRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Ad space not found");

        verify(adSpaceRepository).findById(1L);
        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when start date is not in the future")
    void createBooking_StartDateNotInFuture() {
        // Arrange
        CreateBookingRequestDto invalidRequest = CreateBookingRequestDto.builder()
                .adSpaceId(1L)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(testAdSpace));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.createBooking(invalidRequest))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Start date must be in the future");

        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when end date is not after start date")
    void createBooking_EndDateNotAfterStartDate() {
        // Arrange
        CreateBookingRequestDto invalidRequest = CreateBookingRequestDto.builder()
                .adSpaceId(1L)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(5))
                .build();

        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(testAdSpace));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.createBooking(invalidRequest))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("End date must be after start date");

        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when booking duration is less than 7 days")
    void createBooking_MinimumDurationNotMet() {
        // Arrange
        CreateBookingRequestDto invalidRequest = CreateBookingRequestDto.builder()
                .adSpaceId(1L)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .build();

        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(testAdSpace));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.createBooking(invalidRequest))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Minimum booking duration is 7 days");

        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when ad space is not available")
    void createBooking_AdSpaceNotAvailable() {
        // Arrange
        testAdSpace.setStatus(AdSpaceStatus.BOOKED);
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(testAdSpace));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.createBooking(validBookingRequest))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Ad space is not available");

        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when there are overlapping approved bookings")
    void createBooking_OverlappingBookings() {
        // Arrange
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(testAdSpace));

        BookingRequest overlappingBooking = BookingRequest.builder()
                .id(2L)
                .adSpace(testAdSpace)
                .startDate(LocalDate.now().plusDays(5))
                .endDate(LocalDate.now().plusDays(12))
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingRequestRepository.findOverlappingBookings(
                eq(1L), eq(BookingStatus.APPROVED), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(overlappingBooking));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.createBooking(validBookingRequest))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("approved bookings for the selected date range");

        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should approve pending booking successfully")
    void approveBooking_Success() {
        // Arrange
        BookingRequest pendingBooking = BookingRequest.builder()
                .id(1L)
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .createdAt(LocalDateTime.now())
                .build();

        when(bookingRequestRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
        when(adSpaceRepository.save(any(AdSpace.class))).thenReturn(testAdSpace);
        when(bookingRequestRepository.save(any(BookingRequest.class))).thenReturn(pendingBooking);

        // Act
        BookingResponseDto result = bookingRequestService.approveBooking(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(testAdSpace.getStatus()).isEqualTo(AdSpaceStatus.BOOKED);

        verify(bookingRequestRepository).findById(1L);
        verify(adSpaceRepository).save(testAdSpace);
        verify(bookingRequestRepository).save(pendingBooking);
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when approving non-pending booking")
    void approveBooking_NonPendingStatus() {
        // Arrange
        BookingRequest approvedBooking = BookingRequest.builder()
                .id(1L)
                .adSpace(testAdSpace)
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingRequestRepository.findById(1L)).thenReturn(Optional.of(approvedBooking));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.approveBooking(1L))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Only PENDING bookings can be approved");

        verify(bookingRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject pending booking successfully")
    void rejectBooking_Success() {
        // Arrange
        BookingRequest pendingBooking = BookingRequest.builder()
                .id(1L)
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .createdAt(LocalDateTime.now())
                .build();

        when(bookingRequestRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
        when(bookingRequestRepository.save(any(BookingRequest.class))).thenReturn(pendingBooking);

        // Act
        BookingResponseDto result = bookingRequestService.rejectBooking(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.REJECTED);

        verify(bookingRequestRepository).findById(1L);
        verify(bookingRequestRepository).save(pendingBooking);
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when rejecting non-pending booking")
    void rejectBooking_NonPendingStatus() {
        // Arrange
        BookingRequest rejectedBooking = BookingRequest.builder()
                .id(1L)
                .adSpace(testAdSpace)
                .status(BookingStatus.REJECTED)
                .build();

        when(bookingRequestRepository.findById(1L)).thenReturn(Optional.of(rejectedBooking));

        // Act & Assert
        assertThatThrownBy(() -> bookingRequestService.rejectBooking(1L))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Only PENDING bookings can be rejected");

        verify(bookingRequestRepository, never()).save(any());
    }


}

