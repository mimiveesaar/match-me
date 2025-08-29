import React from "react";

import { Age } from "../../atoms/Match_Cards/Age/age";
import { BioTextbox } from "../../atoms/Match_Cards/Bio/BioBox/biobox";
import { SectionLine } from "../../atoms/Match_Cards/Bio/SectionLine/section_line";
import { MatchCardBackground } from "../../atoms/Match_Cards/MatchCardBackround/match_card_background";
import { MatchCardProfilePic } from "../../atoms/Match_Cards/ProfilePicture/match_card_profile_pic";
import { StarIcon } from "../../atoms/Match_Cards/Star/star_icon";
import { LocationTag } from "../../atoms/Match_Cards/Tags/LocationTag/location_tag";
import { RelationshipTypeTag } from "../../atoms/Match_Cards/Tags/RelationshipTypeTag/relationship_type_tag";
import { Username } from "../../atoms/Match_Cards/UserName/username";
import { ButtonTriangle } from "../../molecules/Match Cards/MatchCardButtons/match_card_buttons";


export interface MatchCardFrontProps {
  profilePicSrc?: string;
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
};

export const MatchCardFront = ({
  profilePicSrc = "default-profile.png",
  alt,
  username = "Shelly",
  age = 64,
  cardColor = "olive",
  location = "Mars",
  lookingFor = "Astral companion",
  bio = "Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.",
  userId,
  onReject,
  onConnectionRequest,
}: MatchCardFrontProps) => {
  return (

    <MatchCardBackground color={cardColor}>
      <div className="flex flex-col items-center h-full relative">

        <div className="relative w-265 h-196">
          <MatchCardProfilePic src={profilePicSrc} alt={alt} />

          <div className="w-full pl-3 pt-1 absolute top-1 right-1 flex gap-1">
            <StarIcon />
          </div>

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
              rejectedId={userId} // Could I make them both use the same id? Like TargetId?
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