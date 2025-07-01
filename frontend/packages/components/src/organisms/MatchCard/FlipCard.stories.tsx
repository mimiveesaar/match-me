import type { Meta, StoryObj } from "@storybook/react";
import { FlipCard } from "./FlipCard";
import { MatchCardFront } from "./MatchCardFront";
import { MatchCardBack } from "./MatchCardBack";
import React from "react";

const meta: Meta<typeof FlipCard> = {
    title: "Organisms/Match Card/FlipCard",
    component: FlipCard,
};
export default meta;

type Story = StoryObj<typeof FlipCard>;

export const Default: Story = {
    render: () =>
        <FlipCard
            front={
                <MatchCardFront location={"Mars"} relationshipType={"Martian lover"} bio={"Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry."}/>
            }
            back={
                <MatchCardBack location={"Mars"} relationshipType={"Martian lover"} bodyform={"Gelatinous"} bio={"Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry."} />
            } 
        />,
};