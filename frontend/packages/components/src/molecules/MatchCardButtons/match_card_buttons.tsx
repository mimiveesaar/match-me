import { LightningButton } from "../../atoms/Match_Cards/Buttons/LightningButton/lightning_button";
import { MoonButton } from "../../atoms/Match_Cards/Buttons/MoonButton/moon_button";
import { SunButton } from "../../atoms/Match_Cards/Buttons/SunButton/sun_button";

export function ButtonTriangle() {
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

