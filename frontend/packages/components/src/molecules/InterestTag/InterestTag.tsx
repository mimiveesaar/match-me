
"use client";
import React from "react";


type InterestTagProps = {
  label: string;
  tag?: string;
};

export const InterestTag = ({ label, tag }: InterestTagProps) => (
  <span
    className="text-xs px-2 py-1 m-1 bg-gray-100 border border-gray-300 rounded shadow-sm"
    data-tag={tag}
  >
    {label}
  </span>
);