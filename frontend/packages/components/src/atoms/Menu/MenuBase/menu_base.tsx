"use client";

import React, { ReactNode } from "react";  

interface MenuBaseProps {
  children: ReactNode;
  className?: string;
}

export const MenuBase = ({ children, className }: MenuBaseProps) => {
  return (
    <div
      className={"w-249 h-800 bg-transparent text-black text-xs text-opacity-80 font-ibm_plex_sans rounded-custom-16 border border-black/70"}
    >
      <div className="h-full w-full p-3">
        {children}
      </div>
    </div>
  );
}