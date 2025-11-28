import React from 'react';
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Box,
  Chip,
} from '@mui/material';
import {
  LocationOn,
  AttachMoney,
  Category,
} from '@mui/icons-material';
import { AdSpace } from '../../types/adspace';
import { StatusChip } from '../common/StatusChip';

interface AdSpaceCardProps {
  adSpace: AdSpace;
  onBook: (adSpace: AdSpace) => void;
  onEdit: (adSpace: AdSpace) => void;
  onDelete: (adSpace: AdSpace) => void;
}

export const AdSpaceCard: React.FC<AdSpaceCardProps> = ({
  adSpace,
  onBook,
  onEdit,
  onDelete,
}) => {
  const isAvailable = adSpace.status === 'AVAILABLE';

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flexGrow: 1 }}>
        <Box display="flex" justifyContent="space-between" alignItems="start" mb={2}>
          <Typography variant="h6" component="div">
            {adSpace.name}
          </Typography>
          <StatusChip status={adSpace.status} />
        </Box>

        <Box display="flex" alignItems="center" gap={1} mb={1}>
          <Category fontSize="small" color="action" />
          <Typography variant="body2" color="text.secondary">
            {adSpace.type.replace('_', ' ')}
          </Typography>
        </Box>

        <Box display="flex" alignItems="center" gap={1} mb={1}>
          <LocationOn fontSize="small" color="action" />
          <Typography variant="body2" color="text.secondary">
            {adSpace.city}
          </Typography>
        </Box>

        <Typography variant="body2" color="text.secondary" mb={2}>
          {adSpace.address}
        </Typography>

        <Box display="flex" alignItems="center" gap={1}>
          <AttachMoney fontSize="small" color="primary" />
          <Typography variant="h6" color="primary">
            ${adSpace.pricePerDay.toFixed(2)}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            / day
          </Typography>
        </Box>
      </CardContent>

      <CardActions>
        <Button
          size="small"
          variant="contained"
          onClick={() => onBook(adSpace)}
          disabled={!isAvailable}
          fullWidth
        >
          Book Now
        </Button>
        <Button
          size="small"
          onClick={() => onEdit(adSpace)}
          disabled
        >
          Edit
        </Button>
        <Button
          size="small"
          color="error"
          onClick={() => onDelete(adSpace)}
          disabled
        >
          Delete
        </Button>
      </CardActions>
    </Card>
  );
};
