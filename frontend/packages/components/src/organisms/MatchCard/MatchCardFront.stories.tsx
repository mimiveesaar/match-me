import type { Meta, StoryObj } from "@storybook/nextjs";
import { MatchCardFront } from "./MatchCardFront";

const meta: Meta<typeof MatchCardFront> = {
  title: "Organisms/Match Card/Match Card Front",
  component: MatchCardFront,
  argTypes: {
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
    cardColor: "olive",
    alt: "Example User",
    location: "Venus",
    bio: "Hi, I'm Shelly! Martian explorer by day, dream weaver by night. Lover of olive lattes and quantum poetry.",
  },
    parameters: {
    layout: "centered", 
  },
};