import React from "react";
import { useRouter } from "next/navigation";

interface PageLinkProps {
  label: string;
  href?: string;
  onClick?: () => void;
  className?: string;
}

export const PageLink = ({ label, href, onClick, className }: PageLinkProps) => {
  const router = useRouter();

  const handleClick = () => {
    if (onClick) {
      onClick(); // Close menu on mobile
    }
    if (href) {
      router.push(href); // Navigate to the page
    }
  };

  return (
    <button
      onClick={handleClick}
      className={`text-left text-lg font-medium text-gray-700 hover:text-olive transition-colors duration-200 py-2 px-3 rounded-lg hover:bg-gray-100 w-full ${className || ''}`}
    >
      {label}
    </button>
  );
};