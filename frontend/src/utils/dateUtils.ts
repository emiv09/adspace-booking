import dayjs, { Dayjs } from 'dayjs';

export const isDateInFuture = (date: Dayjs | string): boolean => {
  const targetDate = dayjs(date);
  const today = dayjs().startOf('day');
  return targetDate.isAfter(today);
};

export const calculateDurationDays = (startDate: Dayjs | string, endDate: Dayjs | string): number => {
  const start = dayjs(startDate);
  const end = dayjs(endDate);
  return end.diff(start, 'day') + 1;
};

export const isMinimumDuration = (startDate: Dayjs | string, endDate: Dayjs | string, minDays: number = 7): boolean => {
  const duration = calculateDurationDays(startDate, endDate);
  return duration >= minDays;
};

export const calculateTotalCost = (startDate: Dayjs | string, endDate: Dayjs | string, pricePerDay: number): number => {
  const duration = calculateDurationDays(startDate, endDate);
  return duration * pricePerDay;
};

export const formatDate = (date: string | Dayjs): string => {
  return dayjs(date).format('YYYY-MM-DD');
};

export const formatDateTime = (dateTime: string | Dayjs): string => {
  return dayjs(dateTime).format('MMM DD, YYYY h:mm A');
};
