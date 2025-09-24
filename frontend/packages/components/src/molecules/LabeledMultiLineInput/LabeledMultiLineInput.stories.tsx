import type { Meta, StoryObj } from "@storybook/nextjs";
import { useState } from "react";
import { LabeledMultiLineInput } from "./LabeledMultiLineInput";

const meta: Meta<typeof LabeledMultiLineInput> = {
  title: "Molecules/LabeledMultiLineInput",
  component: LabeledMultiLineInput,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof LabeledMultiLineInput>;

export const Default: Story = {
  render: (args) => {
    const [value, setValue] = useState("");
    return (
      <LabeledMultiLineInput
        {...args}
        value={value}
        setValue={setValue}
      />
    );
  },
  args: {
    placeholder: "Bio (optional)",
    maxLength: 250,
  },
};
