import React from "react";
import { Check  } from "lucide-react";

export const CheckIcon = ({ onClick }: { onClick?: () => void }) => {
  return (
    <button
      type="button"
      onClick={onClick}
      className="p-3 rounded-full border border-gray-300 shadow-md hover:bg-olive hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-grey-500 cursor-pointer transition"
      aria-label="Submit login"
    >
      <Check className="w-6 h-6 text-gray-700 hover:text-gray-900" />
    </button>
  );
};