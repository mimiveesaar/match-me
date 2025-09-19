"use client";


interface RelationshipTypeTagProps {
  lookingFor: string;
  className?: string;
}

export const RelationshipTypeTag = ({ lookingFor, className = "" }: RelationshipTypeTagProps) => {

  return (
    <span
      title={lookingFor}
      className={`h-17 max-w-[130px] inline-flex items-center gap-2 bg-ivory-90 text-black text-xs font-medium font-noto_serif px-2 rounded-custom-4 overflow-hidden whitespace-nowrap text-ellipsis ${className}`}
    >
      🧲 {lookingFor}
    </span>
  );
}