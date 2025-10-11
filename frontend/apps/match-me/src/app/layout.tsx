"use client";

import { useState } from "react";
import "@styles/globals.css";
import "components/styles";
import { AlienMeetLogo } from "components/atoms";
import { Menu, MobileMenu } from "components/organisms";
import { useFiltersStore } from "./stores/matchStore";
import { usePathname } from "next/navigation";
import { useIsMobile } from "./hooks/useIsMobile";


export default function RootLayout({ children }: { children: React.ReactNode }) {
    const pathname = usePathname();
    const noMenuPages = ["/register", "/"];
    const shouldShowMenu = !noMenuPages.includes(pathname);

    const [menuOpen, setMenuOpen] = useState(false);
    const { filters, setFilters } = useFiltersStore();
    const isMobile = useIsMobile();

    return (
        <html lang="en">
        <body className="antialiased bg-ivory">
        <div className="flex flex-col items-center min-h-screen bg-ivory">
            {/* Header row: burger + logo */}
            <div className="flex w-full max-w-7xl items-center justify-between relative p-4 mx-auto">
                {shouldShowMenu && (
                    <button
                        className="p-2 lg:hidden"
                        onClick={() => setMenuOpen(!menuOpen)}
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            className="w-8 h-8"
                            viewBox="0 0 24 24"
                            fill="currentColor"
                        >
                            <path d="M3 6h18v2H3V6m0 5h18v2H3v-2m0 5h18v2H3v-2z" />
                        </svg>
                    </button>
                )}

                <div className="absolute left-1/2 transform -translate-x-1/2 mt-10 ">
                    <AlienMeetLogo />
                </div>
            </div>

            {/* Main content area */}
            <div className="flex flex-col w-full max-w-7xl gap-4 mx-auto flex-1 lg:flex-row">
                {shouldShowMenu && (
                    <>
                        {/* Desktop menu */}
                        {!isMobile && (
                            <div className="w-full bg-ivory p-4 z-50 mt-6">
                                <Menu
                                    filters={filters}
                                    setFilters={setFilters}
                                    hasUnread={false}
                                />
                            </div>
                        )}

                        {/* Mobile menu */}
                        {isMobile && menuOpen && (
                            <div className="w-full bg-ivory z-40 p-6">
                                <MobileMenu
                                    filters={filters}
                                    setFilters={setFilters}
                                    hasUnread={false}
                                />
                            </div>
                        )}
                    </>
                )}

                {/* Children (content) */}
                <div
                    className={`flex-1 transition-all duration-300 ${
                        isMobile && menuOpen ? "mt-2" : ""
                    }`}
                >
                    {children}
                </div>
            </div>
        </div>
        </body>
        </html>
    );
}
