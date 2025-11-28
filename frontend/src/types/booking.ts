export type BookingStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface Booking {
  id: number;
  adSpaceId: number;
  adSpaceName: string;
  advertiserName: string;
  advertiserEmail: string;
  startDate: string;
  endDate: string;
  status: BookingStatus;
  totalCost: number;
  createdAt: string;
}

export interface CreateBookingRequest {
  adSpaceId: number;
  advertiserName: string;
  advertiserEmail: string;
  startDate: string;
  endDate: string;
}
