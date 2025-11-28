export type AdSpaceType = "BILLBOARD" | "BUS_STOP" | "MALL_DISPLAY" | "TRANSIT_AD";

export type AdSpaceStatus = "AVAILABLE" | "BOOKED" | "MAINTENANCE";

export interface AdSpace {
  id: number;
  name: string;
  type: AdSpaceType;
  city: string;
  address: string;
  pricePerDay: number;
  status: AdSpaceStatus;
}
