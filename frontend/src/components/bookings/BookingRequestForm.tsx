import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Typography,
  Alert,
  CircularProgress,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs, { Dayjs } from 'dayjs';
import { AdSpace } from '../../types/adspace';
import { CreateBookingRequest } from '../../types/booking';
import { useBookingsStore } from '../../store/useBookingsStore';
import { ApiError } from '../../types/api';
import {
  isDateInFuture,
  calculateTotalCost,
  isMinimumDuration,
  formatDate,
} from '../../utils/dateUtils';

interface BookingRequestFormProps {
  open: boolean;
  onClose: () => void;
  adSpace: AdSpace | null;
  onSuccess: () => void;
}

interface FormErrors {
  advertiserName?: string;
  advertiserEmail?: string;
  startDate?: string;
  endDate?: string;
}

export const BookingRequestForm: React.FC<BookingRequestFormProps> = ({
  open,
  onClose,
  adSpace,
  onSuccess,
}) => {
  const createBooking = useBookingsStore((state) => state.createBooking);

  const [advertiserName, setAdvertiserName] = useState('');
  const [advertiserEmail, setAdvertiserEmail] = useState('');
  const [startDate, setStartDate] = useState<Dayjs | null>(null);
  const [endDate, setEndDate] = useState<Dayjs | null>(null);
  const [errors, setErrors] = useState<FormErrors>({});
  const [apiError, setApiError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Reset form when dialog opens
  useEffect(() => {
    if (open) {
      setAdvertiserName('');
      setAdvertiserEmail('');
      setStartDate(null);
      setEndDate(null);
      setErrors({});
      setApiError(null);
    }
  }, [open]);

  const validateEmail = (email: string): boolean => {
    return email.includes('@') && email.includes('.');
  };

  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};

    if (!advertiserName.trim()) {
      newErrors.advertiserName = 'Advertiser name is required';
    }

    if (!advertiserEmail.trim()) {
      newErrors.advertiserEmail = 'Email is required';
    } else if (!validateEmail(advertiserEmail)) {
      newErrors.advertiserEmail = 'Invalid email format';
    }

    if (!startDate) {
      newErrors.startDate = 'Start date is required';
    } else if (!isDateInFuture(startDate)) {
      newErrors.startDate = 'Start date must be in the future';
    }

    if (!endDate) {
      newErrors.endDate = 'End date is required';
    } else if (startDate && !endDate.isAfter(startDate)) {
      newErrors.endDate = 'End date must be after start date';
    }

    if (startDate && endDate && !isMinimumDuration(startDate, endDate, 7)) {
      newErrors.endDate = 'Minimum booking duration is 7 days';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const calculateCost = (): number => {
    if (!startDate || !endDate || !adSpace) {
      return 0;
    }
    return calculateTotalCost(startDate, endDate, adSpace.pricePerDay);
  };

  const handleSubmit = async () => {
    if (!validateForm() || !adSpace || !startDate || !endDate) {
      return;
    }

    setIsSubmitting(true);
    setApiError(null);

    const payload: CreateBookingRequest = {
      adSpaceId: adSpace.id,
      advertiserName: advertiserName.trim(),
      advertiserEmail: advertiserEmail.trim(),
      startDate: formatDate(startDate),
      endDate: formatDate(endDate),
    };

    try {
      await createBooking(payload);
      onSuccess();
      onClose();
    } catch (error) {
      const apiErr = error as ApiError;
      if (apiErr.errors && apiErr.errors.length > 0) {
        setApiError(apiErr.errors.join(', '));
      } else {
        setApiError(apiErr.message);
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!adSpace) {
    return null;
  }

  const totalCost = calculateCost();
  const tomorrow = dayjs().add(1, 'day');

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Book Ad Space</DialogTitle>
      <DialogContent>
        <Box sx={{ mt: 1 }}>
          {/* Ad Space Info */}
          <Box sx={{ mb: 3, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
            <Typography variant="h6" gutterBottom>
              {adSpace.name}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {adSpace.city} • ${adSpace.pricePerDay.toFixed(2)} / day
            </Typography>
          </Box>

          {/* API Error */}
          {apiError && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {apiError}
            </Alert>
          )}

          {/* Form Fields */}
          <TextField
            fullWidth
            label="Advertiser Name"
            value={advertiserName}
            onChange={(e) => setAdvertiserName(e.target.value)}
            error={!!errors.advertiserName}
            helperText={errors.advertiserName}
            sx={{ mb: 2 }}
            disabled={isSubmitting}
          />

          <TextField
            fullWidth
            label="Email"
            type="email"
            value={advertiserEmail}
            onChange={(e) => setAdvertiserEmail(e.target.value)}
            error={!!errors.advertiserEmail}
            helperText={errors.advertiserEmail}
            sx={{ mb: 2 }}
            disabled={isSubmitting}
          />

          <DatePicker
            label="Start Date"
            value={startDate}
            onChange={(newValue) => setStartDate(newValue)}
            minDate={tomorrow}
            disabled={isSubmitting}
            slotProps={{
              textField: {
                fullWidth: true,
                error: !!errors.startDate,
                helperText: errors.startDate,
                sx: { mb: 2 },
              },
            }}
          />

          <DatePicker
            label="End Date"
            value={endDate}
            onChange={(newValue) => setEndDate(newValue)}
            minDate={startDate || tomorrow}
            disabled={isSubmitting}
            slotProps={{
              textField: {
                fullWidth: true,
                error: !!errors.endDate,
                helperText: errors.endDate,
                sx: { mb: 2 },
              },
            }}
          />

          {/* Total Cost */}
          {totalCost > 0 && (
            <Box sx={{ p: 2, bgcolor: 'primary.50', borderRadius: 1 }}>
              <Typography variant="body2" color="text.secondary">
                Total Cost
              </Typography>
              <Typography variant="h5" color="primary">
                ${totalCost.toFixed(2)}
              </Typography>
            </Box>
          )}
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={isSubmitting}>
          Cancel
        </Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={isSubmitting}
          startIcon={isSubmitting ? <CircularProgress size={20} /> : null}
        >
          {isSubmitting ? 'Submitting...' : 'Submit'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};
