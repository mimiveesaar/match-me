"use client";
import React from "react";
import Link from "next/link";

interface PageLinkProps {
  label: string;
  href?: string;
  onClick?: () => void;
  className?: string;
}

export const PageLink = ({ label, href, onClick, className = "" }: PageLinkProps) => {
  const baseStyle = "px-3 py-2 text-xl font-medium cursor-pointer font-serif";

  if (href) {
    return (
      <Link href={href} className={`${baseStyle} ${className}`}>
        {label}
      </Link>
    );
  }

  return (
    <button
      onClick={onClick}
      className={`${baseStyle} ${className}`}
      type="button"
    >
      {label}
    </button>
  );
};