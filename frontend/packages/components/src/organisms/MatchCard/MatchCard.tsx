import { Age } from "../../atoms/Match_Cards/Age/age";
import { BioTextbox } from "../../atoms/Match_Cards/Bio/BioBox/biobox";
import { SectionLine } from "../../atoms/Match_Cards/Bio/SectionLine/section_line";
import { MatchCardBackground } from "../../atoms/Match_Cards/MatchCardBackround/match_card_background";
import { MatchCardProfilePic } from "../../atoms/Match_Cards/ProfilePicture/match_card_profile_pic";
import { StarIcon } from "../../atoms/Match_Cards/Star/star_icon";
import { LocationTag } from "../../atoms/Match_Cards/Tags/LocationTag/location_tag";
import { RelationshipTypeTag } from "../../atoms/Match_Cards/Tags/RelationshipTypeTag/relationship_type_tag";
import { Username } from "../../atoms/Match_Cards/UserName/username";
import { ButtonTriangle } from "../../molecules/MatchCardButtons/match_card_buttons";


type MatchCardProps = {
  profilePicSrc?: string;
  alt?: string;
  cardColor?: "amberglow" | "olive" | "peony" | "minty" | "moss" | "coral";
  location?: string;
};

export function MatchCard({
  profilePicSrc,
  alt,
  cardColor = "olive",
  location = "Mars",
}: MatchCardProps) {
  return (
    <div className="hover:scale-105 transition-transform duration-50">
      <MatchCardBackground color={"olive"}>
        <div className="flex flex-col items-center h-full relative">

          <div className="relative w-265 h-196">
            <MatchCardProfilePic src={"default-profile.png"} alt={alt} />

            <div className="w-full pl-3 pt-1 absolute top-1 right-1 flex gap-1">
              <StarIcon/>
            </div>

            <div className="w-full pl-3 absolute bottom-1 left-1 flex gap-1">
              <Username username="Shelly" />
              <Age age="64" />
            </div>
          </div>

          <div className="grid grid-cols-[auto_auto] items-start w-full pt-1 px-3">
            <div className="flex flex-col pt-1 gap-y-2 items-start text-ivory text-xs font-normal font-ibm_plex_sans">
              <LocationTag location={location} />
              <RelationshipTypeTag relationshipType="Astral companion" />
              /bio
            </div>

            <div className="flex justify-end">
              <ButtonTriangle />
            </div>
          </div>

          <div>
            <SectionLine />
          </div>

          <div className="pt-3">
            <BioTextbox bio={`Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.`}>
            </BioTextbox>
          </div>

        </div>
      </MatchCardBackground>
    </div>
  );
}