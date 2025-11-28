export interface ApiError {
  message: string;
  timestamp?: string;
  path?: string;
  errors?: string[];
}
