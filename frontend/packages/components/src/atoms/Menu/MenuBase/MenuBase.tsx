"use client";

import React, { ReactNode } from "react";

interface MenuBaseProps {
  children: ReactNode;
  className?: string;
}

export const MenuBase = ({ children, className = "" }: MenuBaseProps) => (
  <div
    className={`text-opacity-80 font-ibm_plex_sans rounded-custom-16 w-60 border border-black/70 bg-transparent text-xs text-black md:w-70 lg:w-60 ${className}`}
  >
    {children}
  </div>
);
