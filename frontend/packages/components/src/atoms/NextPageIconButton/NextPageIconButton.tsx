import React from "react";
import { ChevronRight } from "lucide-react";

interface NextPageIconButtonProps {
  disabled?: boolean;
  onClick: () => void;
}

export const NextPageIconButton = ({ onClick, disabled }: NextPageIconButtonProps) => (
  <button
    onClick={onClick}
    className="mt-4 cursor-pointer rounded-full p-3 shadow-md transition hover:shadow-lg hover:brightness-10"
    disabled={disabled}
  >
    <ChevronRight className="h-6 w-6 text-gray-700" />
  </button>
);
