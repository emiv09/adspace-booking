import { create } from 'zustand';
import { AdSpace, AdSpaceType } from '../types/adspace';
import { adSpacesApi } from '../api/adSpacesApi';
import { ApiError } from '../types/api';

export interface AdSpacesFilters {
  type?: AdSpaceType;
  city?: string;
}

interface AdSpacesState {
  adSpaces: AdSpace[];
  isLoading: boolean;
  error: string | null;
  filters: AdSpacesFilters;
  setFilters: (filters: AdSpacesFilters) => void;
  fetchAdSpaces: () => Promise<void>;
}

export const useAdSpacesStore = create<AdSpacesState>((set, get) => ({
  adSpaces: [],
  isLoading: false,
  error: null,
  filters: {},

  setFilters: (filters: AdSpacesFilters) => {
    set({ filters });
  },

  fetchAdSpaces: async () => {
    set({ isLoading: true, error: null });
    try {
      const { filters } = get();
      const adSpaces = await adSpacesApi.getAdSpaces(filters);
      set({ adSpaces, isLoading: false });
    } catch (error) {
      const apiError = error as ApiError;
      set({ 
        error: apiError.message, 
        isLoading: false,
        adSpaces: []
      });
    }
  },
}));
