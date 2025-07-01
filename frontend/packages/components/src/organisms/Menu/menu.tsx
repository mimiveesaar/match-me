"use client";

import { MenuHeader } from "@atoms/Menu/Header/header";
import { MenuBase } from "@atoms/Menu/MenuBase/menu_base";
import { PageLink } from "@atoms/Menu/PageLink/pagelink";
import { SignOutButton } from "@atoms/Menu/SignOut/signout";
import React from "react";

export const Menu = () => (
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
      <PageLink label="Chatspace" />
    </div>

    {/* Spacer pushes sign-out to bottom */}
    <div className="flex-1" />

    {/* Bottom: Sign out centered */}
    <div className="w-full flex justify-center mt-4">
      <SignOutButton />
    </div>
  </MenuBase>
);
