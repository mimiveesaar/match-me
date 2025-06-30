import { CardInterestTag } from "./card_interest_tag";
import type { Meta, StoryObj } from "@storybook/react";

const meta: Meta<typeof CardInterestTag> = {
  title: "Molecules/CardInterestTag",
  component: CardInterestTag,
  tags: ["autodocs"],
  argTypes: {
    tag: { control: "text" },
    label: { control: "text" },
  },
};

export default meta;

type Story = StoryObj<typeof CardInterestTag>;

export const Default: Story = {
  args: {
    label: "Starforging",
    tag: "sci-fi",
  },
};

export const NoTag: Story = {
  args: {
    label: "Cooking",
  },
};
