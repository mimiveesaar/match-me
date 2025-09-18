"use client";

import React from "react";

import { Age } from "../../atoms/MatchCards/Age/age";
import { BioTextbox } from "../../atoms/MatchCards/Bio/BioBox/biobox";
import { SectionLine } from "../../atoms/MatchCards/Bio/SectionLine/section_line";
import { MatchCardBackground } from "../../atoms/MatchCards/MatchCardBackround/match_card_background";
import { StarIcon } from "../../atoms/MatchCards/Star/star_icon";
import { LocationTag } from "../../atoms/MatchCards/Tags/LocationTag/location_tag";
import { RelationshipTypeTag } from "../../atoms/MatchCards/Tags/RelationshipTypeTag/relationship_type_tag";
import { Username } from "../../atoms/MatchCards/Username/username";
import { BodyformTag } from "@atoms/MatchCards/Tags/BodyformTag/bodyform_tag";
import { CardInterestTag } from "@atoms/MatchCards/Tags/InterestTag/card_interest_tag";

interface MatchCardBackProps {
    cardColor?: "amberglow" | "olive" | "peony" | "minty" | "moss" | "coral";
    location: string;
    relationshipType: string;
    bodyform: string;
    bio: string;
};

export const MatchCardBack = ({
    cardColor = "olive",
    location = "Mars",
    relationshipType = "Astral companion",
    bodyform = "Polymorphontes",
    bio = "Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.",
}: MatchCardBackProps) => {
    return (

        <MatchCardBackground color={cardColor}>
            <div className="flex flex-col items-center h-full relative">

                <div className="relative w-full h-12">

                    <div className="w-full pl-3 py-1 absolute top-1 left-0 flex justify-start gap-1">
                        <Username username="Shelly" />
                        <Age age="64" />
                    </div>

                    <div className="w-full pr-3 pt-1 absolute top-1 right-0 flex justify-end gap-1">
                        <StarIcon />
                    </div>
                </div>

                <div className="w-full h-20 pt-3 px-3">
                    <div className="flex flex-wrap gap-2 items-start text-ivory text-xs font-normal font-ibm_plex_sans">
                        <LocationTag location={location} />
                        <RelationshipTypeTag relationshipType={relationshipType} />
                        <BodyformTag bodyform={bodyform} />

                    </div>
                </div>

                <div className="text-ivory text-xs font-normal font-ibm_plex_sans">
                    /bio
                    <SectionLine />
                </div>

                <div className="py-4">
                    <BioTextbox bio={bio} />
                </div>

                <div className="text-ivory text-xs font-normal font-ibm_plex_sans">
                    /interests
                    <SectionLine />
                </div>

                <div className="p-3 flex flex-wrap gap-1 items-center justify-center">
                    <CardInterestTag label="Thrifting" />
                    <CardInterestTag label="Martial arts" />
                    <CardInterestTag label="Foraging mushrooms" />
                    <CardInterestTag label="Gardening" />
                    <CardInterestTag label="Quantum Origami" />
                    <CardInterestTag label="Herbaceous" />
                    <CardInterestTag label="Dancing" />
                    <CardInterestTag label="Cooking" />
                </div>

            </div>
        </MatchCardBackground>

    );
}