"use client";

import { LightningButton, SunButton, MoonButton } from "src/atoms";

type ButtonTriangleProps = {
  rejectedId: string;
  onReject: (rejectedId: string) => void; // pass down from parent
  requestedId: string;
  onConnectionRequest: (connectedId: string) => void;
};

export const ButtonTriangle = ({ rejectedId, onReject, requestedId, onConnectionRequest }: ButtonTriangleProps) => {
  return (
    <div className="relative w-100 h-16">
      <div className="absolute top-0 left-1/2 transform -translate-x-1/2">
        <LightningButton onClick={() => onConnectionRequest(requestedId)} />
      </div>

      <div className="absolute bottom-0 left-0">
        <SunButton />
      </div>

      <div className="absolute bottom-0 right-0">
        <MoonButton onClick={() => onReject(rejectedId)} />
      </div>
    </div>
  );
};

