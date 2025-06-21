
import React from "react";

interface NextPageIconButtonProps {
  onClick: () => void;
}

export const NextPageIconButton = ({ onClick }: NextPageIconButtonProps) => (
  <button onClick={onClick} className="bg-transparent text-2xl font-bold text-gray-700 hover:text-black ">
    &gt;
  </button>
);