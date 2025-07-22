import React from "react";
import { ChevronRight } from "lucide-react";

interface NextPageIconButtonProps {
  onClick: () => void;
}

export const NextPageIconButton = ({ onClick }: NextPageIconButtonProps) => (
  <button
    onClick={onClick}
    className="mt-4 cursor-pointer rounded-full p-3 shadow-md transition hover:shadow-lg hover:brightness-10"
  >
    <ChevronRight className="h-6 w-6 text-gray-700" />
  </button>
);
