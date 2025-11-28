import { apiClient, handleApiError } from './client';
import { Booking, BookingStatus, CreateBookingRequest } from '../types/booking';

export interface GetBookingsParams {
  status?: BookingStatus;
}

export const bookingsApi = {
  createBooking: async (payload: CreateBookingRequest): Promise<Booking> => {
    try {
      const response = await apiClient.post<Booking>('/booking-requests', payload);
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },

  getBookings: async (params?: GetBookingsParams): Promise<Booking[]> => {
    try {
      const response = await apiClient.get<Booking[]>('/booking-requests', { params });
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },

  getBookingById: async (id: number): Promise<Booking> => {
    try {
      const response = await apiClient.get<Booking>(`/booking-requests/${id}`);
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },

  approveBooking: async (id: number): Promise<Booking> => {
    try {
      const response = await apiClient.patch<Booking>(`/booking-requests/${id}/approve`);
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },

  rejectBooking: async (id: number): Promise<Booking> => {
    try {
      const response = await apiClient.patch<Booking>(`/booking-requests/${id}/reject`);
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },
};
