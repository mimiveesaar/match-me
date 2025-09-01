"use client";

import React from "react";

interface AgeProps {
  age: number;
}

export const Age = ({ age }: AgeProps) => (
    <span className="text-ivory text-2xl font-semibold font-ibm_plex_sans drop-shadow-custom-2">
      {age}
    </span>
)