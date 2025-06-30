"use client";

import React, { ReactNode } from "react";
import { Eye } from 'lucide-react';

interface RelationshipTypeTagProps {
  relationshipType: string;
  className?: string;
};

export const RelationshipTypeTag = ({ relationshipType, className = "" }: RelationshipTypeTagProps) => {
 
  return (
    <span className={`h-17 inline-flex items-center gap-2 bg-ivory-90 text-black text-xs font-medium font-noto_serif px-2 rounded-custom-4 ${className}`}>
      🧲 {relationshipType}
    </span>
  );
}