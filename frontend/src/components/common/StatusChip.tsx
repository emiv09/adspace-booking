import React from 'react';
import { Chip } from '@mui/material';
import { BookingStatus } from '../../types/booking';
import { AdSpaceStatus } from '../../types/adspace';

interface StatusChipProps {
  status: BookingStatus | AdSpaceStatus;
}

export const StatusChip: React.FC<StatusChipProps> = ({ status }) => {
  const getColor = () => {
    switch (status) {
      case 'PENDING':
        return 'warning';
      case 'APPROVED':
        return 'success';
      case 'REJECTED':
        return 'error';
      case 'AVAILABLE':
        return 'success';
      case 'BOOKED':
        return 'warning';
      case 'MAINTENANCE':
        return 'error';
      default:
        return 'default';
    }
  };

  return (
    <Chip
      label={status}
      color={getColor()}
      size="small"
      sx={{ fontWeight: 'medium' }}
    />
  );
};
