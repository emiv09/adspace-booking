import React from 'react';
import { Alert, AlertTitle } from '@mui/material';

interface ErrorStateProps {
  error: string;
  title?: string;
}

export const ErrorState: React.FC<ErrorStateProps> = ({ error, title = 'Error' }) => {
  return (
    <Alert severity="error" sx={{ mb: 2 }}>
      <AlertTitle>{title}</AlertTitle>
      {error}
    </Alert>
  );
};
