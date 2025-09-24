"use client";

import { MenuBase, MenuHeader, PageLink, SignOutButton } from "@atoms";
import { FilteringDropdown } from "@molecules";
import React, { useState } from "react";

interface MenuProps {
    hasUnread: boolean;
    filters: any;
    setFilters: (filters: any) => void;
    className?: string;
}

export const Menu = ({filters, setFilters, hasUnread, className}): MenuProps => {
    const [showDropdown, setShowDropdown] = useState(false);

    const handleMatchesClick = () => {
        setShowDropdown(prev => !prev);
    };

    return (
        <MenuBase className="flex flex-col h-full p-7 py-3">
            <div className="w-full flex justify-center mb-5">
                <MenuHeader header="Menu" />
            </div>

            <div className="flex flex-col gap-1 items-start w-full">
                <PageLink label="Matches" onClick={handleMatchesClick} />

                {showDropdown && (
                    <div className="w-full">
                        <FilteringDropdown filters={filters} setFilters={setFilters} />
                    </div>
                )}

                <PageLink label="My Profile" />
                <PageLink label="My Connections" />
                <PageLink label="Chatspace" dot={hasUnread} />
            </div>

            <div className="flex-1" />

            <div className="w-full flex justify-center mt-4">
                <SignOutButton />
            </div>
        </MenuBase>
    );
};