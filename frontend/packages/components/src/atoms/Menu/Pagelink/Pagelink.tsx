"use client";

import Link from "next/link";

interface PageLinkProps {
    label: string;
    href?: string;
    onClick?: () => void;
    className?: string;
    dot?: boolean;
}

export const PageLink = ({ label, href, onClick, className = "", dot = false }: PageLinkProps) => {
    const baseStyle =
        "px-3 py-2 text-xl font-medium cursor-pointer font-serif hover:scale-110 transition-transform";

    const content = (
        <div className="flex items-center gap-2">
            {label}
            {dot && <span className="w-2 h-2 bg-red-500 rounded-full inline-block" />}
        </div>
    );

    if (href) {
        return (
            <Link href={href} className={`${baseStyle} ${className}`}>
                {content}
            </Link>
        );
    }

    return (
        <button onClick={onClick} className={`${baseStyle} ${className}`} type="button">
            {content}
        </button>
    );
};