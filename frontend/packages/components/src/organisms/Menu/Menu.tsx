"use client";

import { MenuBase, MenuHeader, PageLink, SignOutButton } from "src/atoms";


interface MenuProps {
    hasUnread: boolean;
    className?: string;
}

export const Menu = ({ hasUnread }: MenuProps) => (
    <MenuBase className="flex flex-col h-full p-4">
        {/* Top: Header */}
        <div className="w-full flex justify-center mb-10">
            <MenuHeader header="Menu" />
        </div>

        {/* Middle: PageLinks */}
        <div className="flex flex-col gap-3 items-start pl-2">
            <PageLink label="Matches" />
            <PageLink label="My Profile" />
            <PageLink label="My Connections" />
            <PageLink label="Chatspace" dot={hasUnread} />
        </div>

        {/* Spacer pushes sign-out to bottom */}
        <div className="flex-1" />

        <div className="w-full flex justify-center mt-4">
            <SignOutButton />
        </div>
    </MenuBase>
);