import type { Meta, StoryObj } from "@storybook/nextjs";
import { MatchCardFront } from "./MatchCardFront";

const meta: Meta<typeof MatchCardFront> = {
  title: "Organisms/Match Card/Match Card Front",
  component: MatchCardFront,
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

type Story = StoryObj<typeof MatchCardFront>;

export const Default: Story = {
  args: {
    profilePicSrc: "/public/default-profile.png",
    cardColor: "olive",
    alt: "Example User",
    location: "Venus",
    relationshipType: "Astral companion",
    bio: "Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.",
  },
    parameters: {
    layout: "centered", 
  },
};