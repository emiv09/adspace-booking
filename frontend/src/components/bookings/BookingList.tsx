import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Box,
  Typography,
  CircularProgress,
} from '@mui/material';
import { Booking } from '../../types/booking';
import { StatusChip } from '../common/StatusChip';
import { formatDate, formatDateTime } from '../../utils/dateUtils';

interface BookingListProps {
  bookings: Booking[];
  onApprove: (id: number) => void;
  onReject: (id: number) => void;
  isLoading?: boolean;
}

export const BookingList: React.FC<BookingListProps> = ({
  bookings,
  onApprove,
  onReject,
  isLoading = false,
}) => {
  if (bookings.length === 0) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="200px"
      >
        <Typography variant="body1" color="text.secondary">
          No bookings found
        </Typography>
      </Box>
    );
  }

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Ad Space</TableCell>
            <TableCell>Advertiser</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Start Date</TableCell>
            <TableCell>End Date</TableCell>
            <TableCell>Status</TableCell>
            <TableCell align="right">Total Cost</TableCell>
            <TableCell>Created</TableCell>
            <TableCell align="center">Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {bookings.map((booking) => (
            <TableRow key={booking.id}>
              <TableCell>{booking.adSpaceName}</TableCell>
              <TableCell>{booking.advertiserName}</TableCell>
              <TableCell>{booking.advertiserEmail}</TableCell>
              <TableCell>{formatDate(booking.startDate)}</TableCell>
              <TableCell>{formatDate(booking.endDate)}</TableCell>
              <TableCell>
                <StatusChip status={booking.status} />
              </TableCell>
              <TableCell align="right">
                ${booking.totalCost.toFixed(2)}
              </TableCell>
              <TableCell>{formatDateTime(booking.createdAt)}</TableCell>
              <TableCell align="center">
                {booking.status === 'PENDING' && (
                  <Box display="flex" gap={1} justifyContent="center">
                    <Button
                      size="small"
                      variant="contained"
                      color="success"
                      onClick={() => onApprove(booking.id)}
                      disabled={isLoading}
                    >
                      {isLoading ? <CircularProgress size={16} /> : 'Approve'}
                    </Button>
                    <Button
                      size="small"
                      variant="contained"
                      color="error"
                      onClick={() => onReject(booking.id)}
                      disabled={isLoading}
                    >
                      {isLoading ? <CircularProgress size={16} /> : 'Reject'}
                    </Button>
                  </Box>
                )}
                {booking.status !== 'PENDING' && (
                  <Typography variant="body2" color="text.secondary">
                    -
                  </Typography>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};
