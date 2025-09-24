import type { Meta, StoryObj } from "@storybook/nextjs";
import { useState } from "react";
import { LabeledInputField } from "./LabeledInputField";

const meta: Meta<typeof LabeledInputField> = {
  title: "Molecules/LabeledInputField",
  component: LabeledInputField,
  tags: ["autodocs"],
  argTypes: {
    label: { control: "text" },
    placeholder: { control: "text" },
    value: { control: "text" },
    error: { control: "text" },
    disabled: { control: "boolean" },
    onChange: { action: "changed" },
    id: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof LabeledInputField>;

export const Default: Story = {
  render: (args) => {
    const [value, setValue] = useState("");
    return (
      <LabeledInputField
        {...args}
        value={value}
        onChange={e => setValue(e.target.value)}
      />
    );
  },
  args: {
    label: "Planet",
    placeholder: "Planet of Residency",
  },
};
