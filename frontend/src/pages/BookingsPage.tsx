import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Box,
  TextField,
  MenuItem,
  Snackbar,
  Alert,
} from '@mui/material';
import { BookingStatus } from '../types/booking';
import { useBookingsStore } from '../store/useBookingsStore';
import { BookingList } from '../components/bookings/BookingList';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';

const statusOptions: (BookingStatus | 'ALL')[] = ['ALL', 'PENDING', 'APPROVED', 'REJECTED'];

export const BookingsPage: React.FC = () => {
  const {
    bookings,
    isLoading,
    error,
    statusFilter,
    setStatusFilter,
    fetchBookings,
    approveBooking,
    rejectBooking,
  } = useBookingsStore();

  const [localFilter, setLocalFilter] = useState<BookingStatus | 'ALL'>('ALL');
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    fetchBookings();
  }, [fetchBookings]);

  const handleFilterChange = (value: BookingStatus | 'ALL') => {
    setLocalFilter(value);
    if (value === 'ALL') {
      setStatusFilter(undefined);
    } else {
      setStatusFilter(value);
    }
    // Trigger a new fetch with the updated filter
    setTimeout(() => {
      fetchBookings();
    }, 0);
  };

  const handleApprove = async (id: number) => {
    try {
      await approveBooking(id);
      setSuccessMessage('Booking approved successfully!');
      setShowSuccessMessage(true);
    } catch (error) {
      // Error is already handled in the store
      console.error('Failed to approve booking:', error);
    }
  };

  const handleReject = async (id: number) => {
    try {
      await rejectBooking(id);
      setSuccessMessage('Booking rejected successfully!');
      setShowSuccessMessage(true);
    } catch (error) {
      // Error is already handled in the store
      console.error('Failed to reject booking:', error);
    }
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Bookings
      </Typography>

      <Box sx={{ mb: 3 }}>
        <TextField
          select
          label="Filter by Status"
          value={localFilter}
          onChange={(e) => handleFilterChange(e.target.value as BookingStatus | 'ALL')}
          sx={{ minWidth: 200 }}
          size="small"
        >
          {statusOptions.map((status) => (
            <MenuItem key={status} value={status}>
              {status === 'ALL' ? 'All Bookings' : status}
            </MenuItem>
          ))}
        </TextField>
      </Box>

      {error && <ErrorState error={error} />}

      {isLoading ? (
        <LoadingState />
      ) : (
        <BookingList
          bookings={bookings}
          onApprove={handleApprove}
          onReject={handleReject}
          isLoading={isLoading}
        />
      )}

      <Snackbar
        open={showSuccessMessage}
        autoHideDuration={6000}
        onClose={() => setShowSuccessMessage(false)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert
          onClose={() => setShowSuccessMessage(false)}
          severity="success"
          sx={{ width: '100%' }}
        >
          {successMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
};
