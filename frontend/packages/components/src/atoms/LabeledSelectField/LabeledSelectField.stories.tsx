import type { Meta, StoryObj } from "@storybook/nextjs";
import { LabeledSelectField } from "./LabeledSelectField";
import React, { useState } from "react";

const meta: Meta<typeof LabeledSelectField> = {
  title: "Molecules/LabeledSelectField",
  component: LabeledSelectField,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof LabeledSelectField>;

const Template = (args: any) => {
  const [value, setValue] = useState(args.value || "");

  return (
    <LabeledSelectField
      {...args}
      value={value}
      onChange={(e) => setValue(e.target.value)}
    />
  );
};

export const Default: Story = {
  render: Template,
  args: {
    id: "planet",
    label: "/planet",
    value: "",
    options: [
      { value: "earth", label: "Earth" },
      { value: "mars", label: "Mars" },
      { value: "venus", label: "Venus" },
      { value: "kepler22b", label: "Kepler-22b" },
    ],
  },
};
