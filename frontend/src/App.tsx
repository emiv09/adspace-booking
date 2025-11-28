import React, { useState } from 'react';
import { ThemeProvider, CssBaseline } from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { theme } from './theme/theme';
import { AppShell } from './components/layout/AppShell';
import { AdSpacesPage } from './pages/AdSpacesPage';
import { BookingsPage } from './pages/BookingsPage';

type ViewType = 'adspaces' | 'bookings';

function App() {
  const [currentView, setCurrentView] = useState<ViewType>('adspaces');

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <AppShell currentView={currentView} onViewChange={setCurrentView}>
          {currentView === 'adspaces' && <AdSpacesPage />}
          {currentView === 'bookings' && <BookingsPage />}
        </AppShell>
      </LocalizationProvider>
    </ThemeProvider>
  );
}

export default App;
