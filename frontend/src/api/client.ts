import axios, { AxiosError } from 'axios';
import { ApiError } from '../types/api';

const BASE_URL = 'http://localhost:8080/api/v1';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
  },
});

export const handleApiError = (error: unknown): ApiError => {
  if (axios.isAxiosError(error)) {
    const axiosError = error as AxiosError<ApiError>;
    
    if (axiosError.response?.data) {
      return axiosError.response.data;
    }
    
    return {
      message: axiosError.message || 'An unexpected error occurred',
      timestamp: new Date().toISOString(),
    };
  }
  
  return {
    message: error instanceof Error ? error.message : 'An unexpected error occurred',
    timestamp: new Date().toISOString(),
  };
};
