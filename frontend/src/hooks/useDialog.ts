import { useState, useCallback } from 'react';

export interface UseDialogReturn {
  open: boolean;
  handleOpen: () => void;
  handleClose: () => void;
}

export const useDialog = (): UseDialogReturn => {
  const [open, setOpen] = useState(false);

  const handleOpen = useCallback(() => {
    setOpen(true);
  }, []);

  const handleClose = useCallback(() => {
    setOpen(false);
  }, []);

  return {
    open,
    handleOpen,
    handleClose,
  };
};
