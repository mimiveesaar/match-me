import type { Meta, StoryObj } from "@storybook/react";
import { MatchCard } from "./MatchCard";

const meta: Meta<typeof MatchCard> = {
  title: "Organisms/Match Card/Profile",
  component: MatchCard,
  argTypes: {
    profilePicSrc: { control: "text" },
    alt: { control: "text" },
    cardColor: {
      control: { type: "select" },
      options: ["amberglow", "olive", "peony", "minty", "moss", "coral"],
    },
    location: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof MatchCard>;

export const Default: Story = {
  args: {
    profilePicSrc: "/public/default-profile.png",
    cardColor: "olive",
    alt: "Example User",
    location: "Venus",
    relationshipType: "Astral companion",
  },
    parameters: {
    layout: "centered", 
  },
};