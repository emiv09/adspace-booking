package com.generatik.adspace.repository;

import com.generatik.adspace.entity.BookingRequest;
import com.generatik.adspace.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByStatus(BookingStatus status);

    /**
     * Find overlapping approved bookings for a given ad space and date range.
     * Two bookings overlap if: (startDate1 <= endDate2) AND (endDate1 >= startDate2)
     */
    @Query("SELECT br FROM BookingRequest br WHERE br.adSpace.id = :adSpaceId " +
           "AND br.status = :status " +
           "AND br.startDate <= :endDate " +
           "AND br.endDate >= :startDate")
    List<BookingRequest> findOverlappingBookings(
        @Param("adSpaceId") Long adSpaceId,
        @Param("status") BookingStatus status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

