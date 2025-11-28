import React from 'react';
import { Typography, Box } from '@mui/material';
import { AdSpace } from '../../types/adspace';
import { AdSpaceCard } from './AdSpaceCard';

interface AdSpaceListProps {
  adSpaces: AdSpace[];
  onBook: (adSpace: AdSpace) => void;
  onEdit: (adSpace: AdSpace) => void;
  onDelete: (adSpace: AdSpace) => void;
}

export const AdSpaceList: React.FC<AdSpaceListProps> = ({
  adSpaces,
  onBook,
  onEdit,
  onDelete,
}) => {
  if (adSpaces.length === 0) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="200px"
      >
        <Typography variant="body1" color="text.secondary">
          No ad spaces found
        </Typography>
      </Box>
    );
  }

  return (
    <Box
      sx={{
        display: 'grid',
        gap: 3,
        gridTemplateColumns: {
          xs: 'repeat(1, minmax(0, 1fr))',
          sm: 'repeat(2, minmax(0, 1fr))',
          md: 'repeat(3, minmax(0, 1fr))',
        },
      }}
    >
      {adSpaces.map((adSpace) => (
        <Box key={adSpace.id}>
          <AdSpaceCard
            adSpace={adSpace}
            onBook={onBook}
            onEdit={onEdit}
            onDelete={onDelete}
          />
        </Box>
      ))}
    </Box>
  );
};
