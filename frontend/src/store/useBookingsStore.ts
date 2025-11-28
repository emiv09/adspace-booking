import { create } from 'zustand';
import { Booking, BookingStatus, CreateBookingRequest } from '../types/booking';
import { bookingsApi } from '../api/bookingsApi';
import { ApiError } from '../types/api';

interface BookingsState {
  bookings: Booking[];
  isLoading: boolean;
  error: string | null;
  statusFilter?: BookingStatus;
  activeBooking: Booking | null;
  setStatusFilter: (status?: BookingStatus) => void;
  fetchBookings: () => Promise<void>;
  createBooking: (payload: CreateBookingRequest) => Promise<Booking>;
  approveBooking: (id: number) => Promise<void>;
  rejectBooking: (id: number) => Promise<void>;
}

export const useBookingsStore = create<BookingsState>((set, get) => ({
  bookings: [],
  isLoading: false,
  error: null,
  statusFilter: undefined,
  activeBooking: null,

  setStatusFilter: (statusFilter?: BookingStatus) => {
    set({ statusFilter });
  },

  fetchBookings: async () => {
    set({ isLoading: true, error: null });
    try {
      const { statusFilter } = get();
      const bookings = await bookingsApi.getBookings(
        statusFilter ? { status: statusFilter } : undefined
      );
      set({ bookings, isLoading: false });
    } catch (error) {
      const apiError = error as ApiError;
      set({ 
        error: apiError.message, 
        isLoading: false,
        bookings: []
      });
    }
  },

  createBooking: async (payload: CreateBookingRequest): Promise<Booking> => {
    set({ isLoading: true, error: null });
    try {
      const booking = await bookingsApi.createBooking(payload);
      set({ isLoading: false });
      // Refetch bookings to get the latest list
      get().fetchBookings();
      return booking;
    } catch (error) {
      const apiError = error as ApiError;
      set({ 
        error: apiError.message, 
        isLoading: false 
      });
      throw error;
    }
  },

  approveBooking: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await bookingsApi.approveBooking(id);
      set({ isLoading: false });
      // Refetch bookings to get updated status
      get().fetchBookings();
    } catch (error) {
      const apiError = error as ApiError;
      set({ 
        error: apiError.message, 
        isLoading: false 
      });
      throw error;
    }
  },

  rejectBooking: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await bookingsApi.rejectBooking(id);
      set({ isLoading: false });
      // Refetch bookings to get updated status
      get().fetchBookings();
    } catch (error) {
      const apiError = error as ApiError;
      set({ 
        error: apiError.message, 
        isLoading: false 
      });
      throw error;
    }
  },
}));
