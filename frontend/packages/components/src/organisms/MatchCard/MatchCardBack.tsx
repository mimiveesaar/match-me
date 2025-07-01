"use client";

import React from "react";

import { Age } from "../../atoms/Match_Cards/Age/age";
import { BioTextbox } from "../../atoms/Match_Cards/Bio/BioBox/biobox";
import { SectionLine } from "../../atoms/Match_Cards/Bio/SectionLine/section_line";
import { MatchCardBackground } from "../../atoms/Match_Cards/MatchCardBackround/match_card_background";
import { StarIcon } from "../../atoms/Match_Cards/Star/star_icon";
import { LocationTag } from "../../atoms/Match_Cards/Tags/LocationTag/location_tag";
import { RelationshipTypeTag } from "../../atoms/Match_Cards/Tags/RelationshipTypeTag/relationship_type_tag";
import { Username } from "../../atoms/Match_Cards/UserName/username";
import { BodyformTag } from "@atoms/Match_Cards/Tags/BodyformTag/bodyform_tag";
import { InterestTag } from "@molecules/InterestTag/InterestTag";
import { CardInterestTag } from "@atoms/Match_Cards/Tags/InterestTag/card_interest_tag";

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
                    <BioTextbox bio={`Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.`}>
                    </BioTextbox>
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