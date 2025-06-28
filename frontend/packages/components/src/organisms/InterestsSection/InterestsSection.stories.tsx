import React from "react";
import { InterestsSection } from "./InterestsSection";
import type { Meta, StoryObj } from "@storybook/react";

const meta: Meta<typeof InterestsSection> = {
  title: "Organisms/InterestsSection",
  component: InterestsSection,
  tags: ["autodocs"],
};

export default meta;

type Story = StoryObj<typeof InterestsSection>;

export const Default: Story = {};
