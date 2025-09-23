"use client";


import { StarIcon } from "lucide-react";
import React from "react";
import { MatchCardBackground, Username, Age, LocationTag, RelationshipTypeTag, BodyformTag, SectionLine, BioTextbox, CardInterestTag } from "src/atoms";



interface MatchCardBackProps {
    cardColor?: "amberglow" | "olive" | "peony" | "minty" | "moss" | "coral";
    username?: string;
    age?: number;
    location: string;
    lookingFor: string;
    bodyform: string;
    bio: string;
    interests?: string[];
    onHide: () => void;
    supermatch?: boolean;
}

export const MatchCardBack = ({
    cardColor = "olive",
    username = "Shelly",
    age = 64,
    location = "Mars",
    lookingFor = "Astral companion",
    bodyform = "Polymorphontes",
    bio = "Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.",
    interests = ["Quantum Origami", "Dancing", "Cooking"],
    supermatch = false,

}: MatchCardBackProps) => {
    return (

        <MatchCardBackground color={cardColor}>
            <div className="flex flex-col items-center h-full relative">

                <div className="relative w-full h-12">

                    <div className="w-full pl-3 py-1 absolute top-1 left-0 flex justify-start gap-1">
                        <Username username={username} />
                        <Age age={age} />
                    </div>
                    {supermatch && (
                        <div className="w-full pr-3 pt-1 absolute top-1 right-0 flex justify-end gap-1">
                            <StarIcon />
                        </div>
                    )}
                </div>

                <div className="w-full h-20 pt-3 px-3">
                    <div className="flex flex-wrap gap-2 items-start text-ivory text-xs font-normal font-ibm_plex_sans">
                        <LocationTag location={location} />
                        <RelationshipTypeTag lookingFor={lookingFor} />
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
                    {interests.map((interest, index) => (
                        <React.Fragment key={index}>
                            <CardInterestTag label={interest} />
                        </React.Fragment>
                    ))}
                </div>

            </div>
        </MatchCardBackground>

    );
}