# Ad Space Booking Frontend

React + TypeScript single-page UI for browsing ad spaces, filtering inventory, and submitting booking requests against the backend at `http://localhost:8080/api/v1`.

## Stack

- React 18 with Create React App (TypeScript template)
- Material UI + @mui/x-date-pickers
- Zustand for state
- Axios for API access
- Day.js via date utils for validation and cost calculations

## Pages

- **Ad Spaces** – default landing view showing the ad space grid. Includes quick filters (type, city), status chip, price info, and a “Book Now” flow that opens the booking dialog pre-filled for the selected item. On submit, the store posts to the backend, closes the dialog, shows a success snackbar, and refetches spaces to reflect availability changes.
- **Bookings** – tab that lists every booking request with total cost, date range, and status. Provides a status filter plus approve/reject actions when a booking is pending. Each action hits the API, updates the zustand store, and re-renders the table so you immediately see the new booking state.

## Key Components

- `components/adspaces/AdSpaceFilters.tsx` – lightweight form for type/city filters with Apply/Clear actions.
- `components/adspaces/AdSpaceList.tsx` + `AdSpaceCard.tsx` – responsive card grid rendering status chips, pricing, and booking controls.
- `components/bookings/BookingRequestForm.tsx` – dialog with date pickers, validation, and live total cost summary.
- `components/bookings/BookingList.tsx` – (optional tab) table view with inline status chips and approve/reject buttons.
- `store/useAdSpacesStore.ts` and `store/useBookingsStore.ts` – Zustand stores centralizing API calls, filters, mutations, and loading/error flags.

## Quick Start

```bash
cd frontend
npm install
npm start
```

- App runs at `http://localhost:3000`.
- Requires the companion backend running locally on port 8080.

## Scripts

- `npm start` – dev server with hot reload
- `npm test` – interactive test runner
- `npm run build` – production bundle
