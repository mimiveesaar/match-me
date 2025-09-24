import type { Meta, StoryObj } from "@storybook/nextjs";
import { useState } from "react";
import { InputField } from "./InputField";

const meta: Meta<typeof InputField> = {
  title: "Atoms/InputField",
  component: InputField,
  tags: ["autodocs"],
  argTypes: {
    placeholder: { control: "text" },
    value: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof InputField>;

export const Default: Story = {
  render: (args) => {
    const [value, setValue] = useState("");
    return (
      <InputField
        {...args}
        value={value}
        onChange={e => setValue(e.target.value)}
      />
    );
  },
  args: {
    placeholder: "Planet of Residency",
  },
};
