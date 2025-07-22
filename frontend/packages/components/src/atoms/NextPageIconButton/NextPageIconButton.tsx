
import React from "react";
import { ChevronRight } from "lucide-react";

interface NextPageIconButtonProps {
  onClick: () => void;
}

export const NextPageIconButton = ({ onClick }: NextPageIconButtonProps) => (
  <button 
    onClick={onClick}
    className="p-3 rounded-full border border-gray-300 shadow-md hover:bg-olive hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-grey-500 cursor-pointer transition mt-4"
  >
    <ChevronRight className="w-6 h-6 text-gray-700 hover:text-gray-900" />
  </button>
);