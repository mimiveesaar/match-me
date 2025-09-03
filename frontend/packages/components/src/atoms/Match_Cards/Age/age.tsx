"use client";

interface AgeProps {
  age: string;
}

export const Age = ({ age }: AgeProps) => (
    <span className="text-ivory text-2xl font-medium font-ibm_plex_sans">
      {age}
    </span>
)