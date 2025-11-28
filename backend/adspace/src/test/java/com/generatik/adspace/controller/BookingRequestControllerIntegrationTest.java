package com.generatik.adspace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generatik.adspace.dto.booking.CreateBookingRequestDto;
import com.generatik.adspace.entity.*;
import com.generatik.adspace.repository.AdSpaceRepository;
import com.generatik.adspace.repository.BookingRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class BookingRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdSpaceRepository adSpaceRepository;

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    private AdSpace testAdSpace;

    @BeforeEach
    void setUp() {
        // Clean up
        bookingRequestRepository.deleteAll();
        adSpaceRepository.deleteAll();

        // Create test ad space
        testAdSpace = AdSpace.builder()
                .name("Billboard Downtown")
                .type(AdSpaceType.BILLBOARD)
                .city("New York")
                .address("123 Main St")
                .pricePerDay(BigDecimal.valueOf(100))
                .status(AdSpaceStatus.AVAILABLE)
                .build();
        testAdSpace = adSpaceRepository.save(testAdSpace);
    }

    @Test
    @DisplayName("POST /api/v1/booking-requests - Should create booking successfully with 201")
    void createBooking_Success() throws Exception {
        // Arrange
        CreateBookingRequestDto request = CreateBookingRequestDto.builder()
                .adSpaceId(testAdSpace.getId())
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/booking-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.advertiserName").value("John Doe"))
                .andExpect(jsonPath("$.advertiserEmail").value("john.doe@example.com"))
                .andExpect(jsonPath("$.adSpaceId").value(testAdSpace.getId()))
                .andExpect(jsonPath("$.totalCost").value(800));
    }

    @Test
    @DisplayName("POST /api/v1/booking-requests - Should return 400 when required fields are missing")
    void createBooking_ValidationError_MissingFields() throws Exception {
        // Arrange
        CreateBookingRequestDto request = CreateBookingRequestDto.builder()
                .adSpaceId(testAdSpace.getId())
                // Missing required fields
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/booking-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("POST /api/v1/booking-requests - Should return 400 when email is invalid")
    void createBooking_ValidationError_InvalidEmail() throws Exception {
        // Arrange
        CreateBookingRequestDto request = CreateBookingRequestDto.builder()
                .adSpaceId(testAdSpace.getId())
                .advertiserName("John Doe")
                .advertiserEmail("invalid-email")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/booking-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("POST /api/v1/booking-requests - Should return 400 when booking duration is less than 7 days")
    void createBooking_BusinessValidationError_MinimumDuration() throws Exception {
        // Arrange
        CreateBookingRequestDto request = CreateBookingRequestDto.builder()
                .adSpaceId(testAdSpace.getId())
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/booking-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Minimum booking duration")));
    }

    @Test
    @DisplayName("GET /api/v1/booking-requests/{id} - Should return booking details with 200")
    void getBookingById_Success() throws Exception {
        // Arrange
        BookingRequest booking = BookingRequest.builder()
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .build();
        booking = bookingRequestRepository.save(booking);

        // Act & Assert
        mockMvc.perform(get("/api/v1/booking-requests/" + booking.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.advertiserName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/v1/booking-requests/{id} - Should return 404 when booking not found")
    void getBookingById_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/booking-requests/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("not found")));
    }

    @Test
    @DisplayName("PATCH /api/v1/booking-requests/{id}/approve - Should approve booking with 200")
    void approveBooking_Success() throws Exception {
        // Arrange
        BookingRequest booking = BookingRequest.builder()
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .build();
        booking = bookingRequestRepository.save(booking);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/booking-requests/" + booking.getId() + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @DisplayName("PATCH /api/v1/booking-requests/{id}/approve - Should return 400 when booking is not pending")
    void approveBooking_InvalidTransition() throws Exception {
        // Arrange
        BookingRequest booking = BookingRequest.builder()
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.APPROVED)
                .totalCost(BigDecimal.valueOf(800))
                .build();
        booking = bookingRequestRepository.save(booking);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/booking-requests/" + booking.getId() + "/approve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Only PENDING bookings can be approved")));
    }

    @Test
    @DisplayName("PATCH /api/v1/booking-requests/{id}/reject - Should reject booking with 200")
    void rejectBooking_Success() throws Exception {
        // Arrange
        BookingRequest booking = BookingRequest.builder()
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .build();
        booking = bookingRequestRepository.save(booking);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/booking-requests/" + booking.getId() + "/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @DisplayName("GET /api/v1/booking-requests - Should return all bookings")
    void getAllBookings_Success() throws Exception {
        // Arrange
        BookingRequest booking1 = BookingRequest.builder()
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .build();
        bookingRequestRepository.save(booking1);

        // Act & Assert
        mockMvc.perform(get("/api/v1/booking-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("GET /api/v1/booking-requests?status=PENDING - Should filter by status")
    void getAllBookings_FilterByStatus() throws Exception {
        // Arrange
        BookingRequest pendingBooking = BookingRequest.builder()
                .adSpace(testAdSpace)
                .advertiserName("John Doe")
                .advertiserEmail("john.doe@example.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(8))
                .status(BookingStatus.PENDING)
                .totalCost(BigDecimal.valueOf(800))
                .build();
        bookingRequestRepository.save(pendingBooking);

        // Act & Assert
        mockMvc.perform(get("/api/v1/booking-requests?status=PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].status", everyItem(is("PENDING"))));
    }

}

