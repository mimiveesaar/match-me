import type { Meta, StoryObj } from "@storybook/nextjs";
import { RelationshipTypeTag } from "./RelationshipTypeTag";

const meta: Meta<typeof RelationshipTypeTag> = {
    title: "Atoms/MatchCards/RelationshipTypeTag",
    component: RelationshipTypeTag,
    argTypes: {
        lookingFor: { control: "text" },
    },
};
export default meta;

type Story = StoryObj<typeof RelationshipTypeTag>;

export const Default: Story = {
    args: {
        lookingFor: "Astral Companion",
    },
    parameters: {
        layout: "centered",
    },
};
