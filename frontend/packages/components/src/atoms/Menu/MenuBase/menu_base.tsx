"use client";

import React, { ReactNode } from "react";  

interface MenuBaseProps {
  children: ReactNode;
  className?: string;
}

export const MenuBase = ({ children, className = "" }: MenuBaseProps) => (

    <div
      className={`w-[265px] h-screen m-10 mt-28 bg-transparent text-black text-xs text-opacity-80 font-ibm_plex_sans rounded-custom-16 border border-black/70 ${className}`}
    > 
      {children}
    </div>
)