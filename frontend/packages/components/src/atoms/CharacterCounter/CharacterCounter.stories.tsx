import type { Meta, StoryObj } from "@storybook/react";
import { CharacterCounter } from "./CharacterCounter";

const meta: Meta<typeof CharacterCounter> = {
  title: "Atoms/CharacterCounter",
  component: CharacterCounter,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof CharacterCounter>;

export const Default: Story = {
  args: {
    current: 87,
    max: 250,
  },
};
