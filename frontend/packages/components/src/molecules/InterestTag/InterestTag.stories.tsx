import React from "react";
import { InterestTag } from "./InterestTag";
import type { Meta, StoryObj } from "@storybook/nextjs";

const meta: Meta<typeof InterestTag> = {
  title: "Molecules/InterestTag",
  component: InterestTag,
  tags: ["autodocs"],
  argTypes: {
    tag: { control: "text" },
    label: { control: "text" },
  },
};

export default meta;

type Story = StoryObj<typeof InterestTag>;

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
