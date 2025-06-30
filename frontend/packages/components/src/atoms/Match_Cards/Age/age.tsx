"use client";

import React, { ReactNode } from "react";

interface AgeProps {
  age: string;
};

export const Age = ({ age }: AgeProps) => {
  
  return (
    <span className="text-ivory text-2xl font-medium font-ibm_plex_sans">
      {age}
    </span>
  );
}