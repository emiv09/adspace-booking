import { apiClient, handleApiError } from './client';
import { AdSpace, AdSpaceType } from '../types/adspace';

export interface GetAdSpacesParams {
  type?: AdSpaceType;
  city?: string;
}

export const adSpacesApi = {
  getAdSpaces: async (params?: GetAdSpacesParams): Promise<AdSpace[]> => {
    try {
      const response = await apiClient.get<AdSpace[]>('/ad-spaces', { params });
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },

  getAdSpaceById: async (id: number): Promise<AdSpace> => {
    try {
      const response = await apiClient.get<AdSpace>(`/ad-spaces/${id}`);
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },
};
