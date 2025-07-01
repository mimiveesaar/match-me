"use client";

import { MenuHeader } from "@atoms/Menu/Header/header";
import { MenuBase } from "@atoms/Menu/MenuBase/menu_base";
import { PageLink } from "@atoms/Menu/PageLink/pagelink";
import { SignOutButton } from "@atoms/Menu/SignOut/signout";
import React from "react";

export const Menu = () => (

  <MenuBase className="flex flex-col justify-between items-center p-4">
    <div className="w-full flex justify-center">
      <MenuHeader header="Menu" />
    </div>

    <div className="flex-1 w-full flex justify-center items-center">
      <PageLink label={"Matches"}>
        
      </PageLink>
    </div>

    <div className="w-full flex justify-center">
      <SignOutButton />
    </div>
  </MenuBase>
);