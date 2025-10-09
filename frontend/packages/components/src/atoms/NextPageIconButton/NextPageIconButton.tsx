import React from "react";
import { ChevronRight } from "lucide-react";

interface NextPageIconButtonProps {
  disabled?: boolean;
  onClick: () => void;
}

export const NextPageIconButton = ({ onClick, disabled }: NextPageIconButtonProps) => (
  <button
    onClick={onClick}
    className="mt-4 cursor-pointer p-3 transition hover:scale-125"
    disabled={disabled}
  >
    <ChevronRight className="h-8 w-8 text-gray-700" />
  </button>
);
