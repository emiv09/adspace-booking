import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Snackbar,
  Alert,
} from '@mui/material';
import { AdSpace } from '../types/adspace';
import { useAdSpacesStore } from '../store/useAdSpacesStore';
import { useDialog } from '../hooks/useDialog';
import { AdSpaceFilters } from '../components/adspaces/AdSpaceFilters';
import { AdSpaceList } from '../components/adspaces/AdSpaceList';
import { BookingRequestForm } from '../components/bookings/BookingRequestForm';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';

export const AdSpacesPage: React.FC = () => {
  const {
    adSpaces,
    isLoading,
    error,
    filters,
    setFilters,
    fetchAdSpaces,
  } = useAdSpacesStore();

  const bookingDialog = useDialog();
  const [selectedAdSpace, setSelectedAdSpace] = useState<AdSpace | null>(null);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);

  useEffect(() => {
    fetchAdSpaces();
  }, [fetchAdSpaces]);

  const handleFiltersChange = (newFilters: typeof filters) => {
    setFilters(newFilters);
  };

  const handleApplyFilters = () => {
    fetchAdSpaces();
  };

  const handleBook = (adSpace: AdSpace) => {
    setSelectedAdSpace(adSpace);
    bookingDialog.handleOpen();
  };

  const handleEdit = (adSpace: AdSpace) => {
    // Placeholder - not implemented
    console.log('Edit ad space:', adSpace);
  };

  const handleDelete = (adSpace: AdSpace) => {
    // Placeholder - not implemented
    console.log('Delete ad space:', adSpace);
  };

  const handleBookingSuccess = () => {
    setShowSuccessMessage(true);
    // Optionally refresh ad spaces to update status
    fetchAdSpaces();
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Ad Spaces
      </Typography>

      <AdSpaceFilters
        filters={filters}
        onFiltersChange={handleFiltersChange}
        onApply={handleApplyFilters}
      />

      {error && <ErrorState error={error} />}

      {isLoading ? (
        <LoadingState />
      ) : (
        <AdSpaceList
          adSpaces={adSpaces}
          onBook={handleBook}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      )}

      <BookingRequestForm
        open={bookingDialog.open}
        onClose={bookingDialog.handleClose}
        adSpace={selectedAdSpace}
        onSuccess={handleBookingSuccess}
      />

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
          Booking created successfully!
        </Alert>
      </Snackbar>
    </Container>
  );
};
