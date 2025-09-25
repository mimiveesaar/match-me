import type { Meta, StoryObj } from "@storybook/nextjs";
import { useState } from "react";
import { MultiLineInputField } from "./MultiLineInputField";

const meta: Meta<typeof MultiLineInputField> = {
  title: "Atoms/MultiLineInputField",
  component: MultiLineInputField,
  tags: ["autodocs"],
  argTypes: {
    placeholder: { control: "text" },
    value: { control: "text" },
    maxLength: { control: "number" },
    rows: { control: "number" },
  },
};
export default meta;

type Story = StoryObj<typeof MultiLineInputField>;

export const Default: Story = {
  render: (args) => {
    const [value, setValue] = useState("");
    return (
      <MultiLineInputField
        {...args}
        value={value}
        onChange={(e) => setValue(e.target.value)}
      />
    );
  },
  args: {
    placeholder: "Write your bio here...",
    maxLength: 250,
    rows: 4,
  },
};
