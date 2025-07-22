import React from "react";
import { Key } from "lucide-react";

export const KeyIcon = ({ onClick }) => {
  return (
    <button
      type="button"
      onClick={onClick}
      className="mt-4 cursor-pointer rounded-full p-3 shadow-md transition hover:shadow-lg hover:brightness-10"
      aria-label="Submit login"
    >
      <Key className="h-6 w-6 text-gray-700" />
    </button>
  );
};
