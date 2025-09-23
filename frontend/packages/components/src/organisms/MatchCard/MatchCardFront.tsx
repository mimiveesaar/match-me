"use client";

import React from "react";
import { Age, BioTextbox, LocationTag, MatchCardBackground, MatchCardProfilePic, RelationshipTypeTag, SectionLine, StarIcon, Username } from "@atoms";
import { ButtonTriangle } from "@molecules";


export interface MatchCardFrontProps {
  profilepicSrc?: string;
  alt?: string;
  username?: string;
  age?: number;
  cardColor?: "amberglow" | "olive" | "peony" | "minty" | "moss" | "coral";
  location: string;
  lookingFor: string;
  bio?: string;
  userId: string;
  onReject: (userId: string) => void;
  onConnectionRequest: (userId: string) => void;
  supermatch?: boolean;
}

export const MatchCardFront = ({
  profilepicSrc,
  alt,
  username,
  age,
  cardColor,
  location,
  lookingFor,
  bio,
  userId,
  onReject,
  onConnectionRequest,
  supermatch,
}: MatchCardFrontProps) => {
  return (

    <MatchCardBackground color={cardColor}>
      <div className="flex flex-col items-center h-full relative">

        <div className="relative w-265 h-196">
          <MatchCardProfilePic src={profilepicSrc} alt={alt} />

          {supermatch && (   // only render if true
            <div className="w-full pl-3 pt-1 absolute top-1 right-1 flex gap-1">
              <StarIcon />
            </div>
          )}

          <div className="w-full pl-3 absolute bottom-1 left-1 flex gap-1">
            <Username username={username} />
            <Age age={age} />
          </div>
        </div>

        <div className="grid grid-cols-[auto_auto] items-start w-full pt-1 px-3">
          <div className="flex flex-col pt-1 gap-y-2 items-start text-ivory text-xs font-normal font-ibm_plex_sans">
            <LocationTag location={location} />
            <RelationshipTypeTag lookingFor={lookingFor} />
            /bio
          </div>

          <div className="flex justify-end">
            <ButtonTriangle
              rejectedId={userId}
              onReject={onReject}
              requestedId={userId}
              onConnectionRequest={onConnectionRequest}
            />
          </div>
        </div>

        <div>
          <SectionLine />
        </div>

        <div className="pt-3">
          <BioTextbox bio={bio}>
          </BioTextbox>
        </div>

      </div>
    </MatchCardBackground>
  );
}