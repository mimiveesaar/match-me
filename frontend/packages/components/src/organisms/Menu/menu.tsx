"use client";

import React, { useState } from "react";
import { MenuHeader } from "@atoms/Menu/Header/header";
import { MenuBase } from "@atoms/Menu/MenuBase/menu_base";
import { PageLink } from "@atoms/Menu/PageLink/pagelink";
import { SignOutButton } from "@atoms/Menu/SignOut/signout";
import { FilteringDropdown } from "@molecules/Menu/filtering_dropdown";


export const Menu = () => {
  const [showDropdown, setShowDropdown] = useState(false);

  const handleMatchesClick = () => {
    setShowDropdown(prev => !prev);
  };

  return (
    <MenuBase className="flex flex-col h-full p-4">
  
      <div className="w-full flex justify-center mb-10">
        <MenuHeader header="Menu" />
      </div>

      <div className="flex flex-col gap-3 items-start  w-full">
        <button onClick={handleMatchesClick}>
          <PageLink label="Matches" />
        </button>
        {showDropdown && (
          <div className="w-full">
            <FilteringDropdown />
          </div>
        )}

        <PageLink label="My Profile" />
        <PageLink label="My Connections" />
        <PageLink label="Chatspace" />
      </div>

      <div className="flex-1" />

      <div className="w-full flex justify-center mt-4">
        <SignOutButton />
      </div>
    </MenuBase>
  );
};