"use client";

import React from "react";

interface InterestTagProps {
  label: string;
  tag?: string;
  shadow?: boolean;
};

export const InterestTag = ({ label, tag, shadow = false }: InterestTagProps) => (
  <span
    className="text-xs px-2 py-1 m-1 bg-ivory rounded-custom-4 drop-shadow-custom-2"
    data-tag={tag}
  >
    {label}
  </span>
);