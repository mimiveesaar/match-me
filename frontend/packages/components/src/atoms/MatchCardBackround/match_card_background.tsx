import React from "react";


export function CardBackground({ children }) {
  return (
    <div className="bg-pink-100 w-72 h-96 rounded-3xl p-4 drop-shadow-[-4px_4px_4px_rgba(0,0,0,0.25)]">
      {children}
    </div>
  );
}