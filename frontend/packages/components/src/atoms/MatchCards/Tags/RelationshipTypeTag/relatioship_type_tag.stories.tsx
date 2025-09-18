import type { Meta, StoryObj } from "@storybook/nextjs";
import { RelationshipTypeTag } from "./relationship_type_tag";

const meta: Meta<typeof RelationshipTypeTag> = {
  title: "Atoms/Match Cards/Relationship Type Tag",
  component: RelationshipTypeTag,
  argTypes: {
    relationshipType: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof RelationshipTypeTag>;

export const Default: Story = {
  args: {
    relationshipType: "Astral Companion",
  },
  parameters: {
    layout: "centered", 
  },
};
