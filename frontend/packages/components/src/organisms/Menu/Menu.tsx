"use client";

import { usePathname, useRouter } from "next/navigation"; // <-- needed
import { MenuBase, MenuHeader, PageLink, SignOutButton } from "@atoms";
import { FilteringDropdown } from "@molecules";
import React, {useEffect, useState } from "react";

interface MenuProps {
    hasUnread: boolean;
    filters: any;
    setFilters: (filters: any) => void;
    className?: string;
}

export const Menu = ({ filters, setFilters, hasUnread, className }: MenuProps) => {
    const [showDropdown, setShowDropdown] = useState(false);
    const pathname = usePathname();
    const router = useRouter();

    const handleMatchesClick = () => {
        if (pathname === "/matches") {
            setShowDropdown(prev => !prev);
        } else {
            router.push("/matches");
        }
    };

    useEffect(() => {
        if (pathname !== "/matches") {
            setShowDropdown(false);
        }
    }, [pathname]);

    return (
        <MenuBase className="flex flex-col h-full p-7 py-3">
            <div className="w-full flex justify-center mb-5">
                <MenuHeader header="Menu" />
            </div>

            <div className="flex flex-col gap-1 items-start w-full">
                <PageLink label="Matches" onClick={handleMatchesClick} />

                {pathname === "/matches" && showDropdown && (
                    <div className="w-full">
                        <FilteringDropdown filters={filters} setFilters={setFilters} />
                    </div>
                )}

                <PageLink label="My Profile" href="/profile" />
                <PageLink label="My Connections" href="/connections" />
                <PageLink label="Chatspace" href="/chat" dot={hasUnread} />
            </div>

            <div className="flex-1" />

            <div className="w-full flex justify-center mt-4">
                <SignOutButton />
            </div>
        </MenuBase>
    );
};
