"use client";

import React from "react";

interface CardInterestTagProps {
  label: string;
  tag?: string;
}

export const CardInterestTag = ({ label, tag }: CardInterestTagProps) => (
  <span
    className="h-4 px-2 py-1 m-1 bg-ivory rounded-custom-4 text-xs flex items-center justify-center"
    data-tag={tag}
  >
    {label}
  </span>
);