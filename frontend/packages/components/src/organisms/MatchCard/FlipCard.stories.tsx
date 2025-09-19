import type { Meta, StoryObj } from "@storybook/nextjs";
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
                <MatchCardFront location={"Mars"}
                                bio={"Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry."}
                                lookingFor={""} userId={""} onReject={function (userId: string): void {
                    throw new Error("Function not implemented.");
                }} onConnectionRequest={function (userId: string): void {
                    throw new Error("Function not implemented.");
                }}/>
            }
            back={
                <MatchCardBack location={"Mars"} bodyform={"Gelatinous"}
                               bio={"Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry."}
                               lookingFor={""} onHide={function (): void {
                    throw new Error("Function not implemented.");
                }} />
            } 
        />,
};