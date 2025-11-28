import React, { useState } from 'react';
import {
  Box,
  TextField,
  MenuItem,
  Button,
} from '@mui/material';
import { AdSpaceType } from '../../types/adspace';
import { AdSpacesFilters } from '../../store/useAdSpacesStore';

interface AdSpaceFiltersProps {
  filters: AdSpacesFilters;
  onFiltersChange: (filters: AdSpacesFilters) => void;
  onApply: () => void;
}

const adSpaceTypes: AdSpaceType[] = ['BILLBOARD', 'BUS_STOP', 'MALL_DISPLAY', 'TRANSIT_AD'];

export const AdSpaceFilters: React.FC<AdSpaceFiltersProps> = ({
  filters,
  onFiltersChange,
  onApply,
}) => {
  const [localType, setLocalType] = useState<AdSpaceType | ''>(filters.type || '');
  const [localCity, setLocalCity] = useState<string>(filters.city || '');

  const handleApply = () => {
    onFiltersChange({
      type: localType || undefined,
      city: localCity || undefined,
    });
    onApply();
  };

  const handleClear = () => {
    setLocalType('');
    setLocalCity('');
    onFiltersChange({});
    onApply();
  };

  return (
    <Box sx={{ mb: 3 }}>
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: {
            xs: '1fr',
            sm: 'repeat(3, minmax(0, 1fr))',
          },
          gap: 2,
          alignItems: 'center',
        }}
      >
        <TextField
          select
          fullWidth
          label="Ad Space Type"
          value={localType}
          onChange={(e) => setLocalType(e.target.value as AdSpaceType | '')}
          size="small"
        >
          <MenuItem value="">All Types</MenuItem>
          {adSpaceTypes.map((type) => (
            <MenuItem key={type} value={type}>
              {type.replace('_', ' ')}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          fullWidth
          label="City"
          value={localCity}
          onChange={(e) => setLocalCity(e.target.value)}
          size="small"
          placeholder="Enter city name"
        />

        <Box display="flex" gap={1} width="100%">
          <Button
            variant="contained"
            onClick={handleApply}
            fullWidth
          >
            Apply
          </Button>
          <Button
            variant="outlined"
            onClick={handleClear}
            fullWidth
          >
            Clear
          </Button>
        </Box>
      </Box>
    </Box>
  );
};
