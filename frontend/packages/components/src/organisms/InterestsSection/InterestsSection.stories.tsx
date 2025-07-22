import React from "react";
import { InterestsSection } from "./InterestsSection";
import type { Meta, StoryObj } from "@storybook/nextjs";

const meta: Meta<typeof InterestsSection> = {
  title: "Organisms/InterestsSection",
  component: InterestsSection,
  tags: ["autodocs"],
};

export default meta;

type Story = StoryObj<typeof InterestsSection>;

export const Default: Story = {
  render: (args) => {
    const [selected, setSelected] = React.useState<string[]>([]);
    return <InterestsSection {...args} selected={selected} setSelected={setSelected} />;
  }
};