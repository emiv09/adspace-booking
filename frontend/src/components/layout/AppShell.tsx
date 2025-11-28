import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
} from '@mui/material';
import { Store, CalendarMonth } from '@mui/icons-material';

interface AppShellProps {
  currentView: 'adspaces' | 'bookings';
  onViewChange: (view: 'adspaces' | 'bookings') => void;
  children: React.ReactNode;
}

export const AppShell: React.FC<AppShellProps> = ({
  currentView,
  onViewChange,
  children,
}) => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Ad Space Booking
          </Typography>
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button
              color="inherit"
              startIcon={<Store />}
              onClick={() => onViewChange('adspaces')}
              variant={currentView === 'adspaces' ? 'outlined' : 'text'}
              sx={{
                borderColor: currentView === 'adspaces' ? 'white' : 'transparent',
              }}
            >
              Ad Spaces
            </Button>
            <Button
              color="inherit"
              startIcon={<CalendarMonth />}
              onClick={() => onViewChange('bookings')}
              variant={currentView === 'bookings' ? 'outlined' : 'text'}
              sx={{
                borderColor: currentView === 'bookings' ? 'white' : 'transparent',
              }}
            >
              Bookings
            </Button>
          </Box>
        </Toolbar>
      </AppBar>
      <Box component="main" sx={{ flexGrow: 1, bgcolor: 'background.default' }}>
        {children}
      </Box>
    </Box>
  );
};
