"use client";

import React, { ReactNode } from "react";
import { LightningButton } from "@atoms/MatchCards/Buttons/LightningButton/lightning_button";
import { MoonButton } from "@atoms/MatchCards/Buttons/MoonButton/moon_button";
import { SunButton } from "@atoms/MatchCards/Buttons/SunButton/sun_button";

export const ButtonTriangle = () => {
  return (
    <div className="relative w-100 h-16">
      {/* Lightning top center */}
      <div className="absolute top-0 left-1/2 transform -translate-x-1/2">
        <LightningButton />
      </div>

      {/* Sun bottom left */}
      <div className="absolute bottom-0 left-0">
        <SunButton />
      </div>

      {/* Moon bottom right */}
      <div className="absolute bottom-0 right-0">
        <MoonButton />
      </div>
    </div>
  );
}

