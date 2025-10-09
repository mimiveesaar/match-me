"use client";

import { useState } from "react";
import "@styles/globals.css";
import "components/styles";
import { AlienMeetLogo } from "components/atoms";
import { Menu } from "components/organisms";
import { useFiltersStore } from "./stores/matchStore";
import { usePathname } from "next/navigation";


export default function RootLayout({ children }: { children: React.ReactNode }) {

    const pathname = usePathname();
    const noMenuPages = ['/register'];
    const shouldShowMenu = !noMenuPages.includes(pathname);

    const [menuOpen, setMenuOpen] = useState(false);

    // grab state + updater from zustand
    const { filters, setFilters } = useFiltersStore();


    return (
        <html lang="en">
        <body className={`antialiased bg-ivory`}>
        <div className="flex flex-col items-center min-h-screen bg-ivory">

            {/* Header row: burger + logo */}

            <div className="flex w-full max-w-7xl items-center justify-between relative p-4 mx-auto">
                {shouldShowMenu && (
                    <button className="p-2" onClick={() => setMenuOpen(!menuOpen)}>
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


                <div className="absolute left-1/2 transform -translate-x-1/2">
                    <AlienMeetLogo />
                </div>
            </div>

            {/* Main content toggleable menu */}
            <div className="flex w-full max-w-7xl gap-4 mx-auto flex-1 ">
                { menuOpen && (
                    <div className="w-full max-w-xs bg-ivory p-4 z-50 mt-6">
                        <Menu
                            filters={filters}
                            setFilters={setFilters}
                            hasUnread={false} />
                    </div>
                )}

                <div className="flex-1">
                    {children}
                </div>
            </div>
        </div>
        </body>
        </html>
    );
}
