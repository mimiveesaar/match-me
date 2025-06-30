import type { Meta, StoryObj } from "@storybook/react";
import { MatchCardBack } from "./MatchCardBack";

const meta: Meta<typeof MatchCardBack> = {
  title: "Organisms/Match Card/Match Card Back",
  component: MatchCardBack,
  argTypes: {
    cardColor: {
      control: { type: "select" },
      options: ["amberglow", "olive", "peony", "minty", "moss", "coral"],
    },
    location: { control: "text" },
    bodyform: { control: "text" },
    bio: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof MatchCardBack>;

export const Default: Story = {
  args: {
    cardColor: "olive",
    location: "Venus",
    relationshipType: "Astral companion",
    bodyform: "Polymorphontes",
    bio: "A cosmic wanderer seeking a partner to explore the mysteries of the universe. Loves stargazing and interstellar travel.",

  },
    parameters: {
    layout: "centered", 
  },
};