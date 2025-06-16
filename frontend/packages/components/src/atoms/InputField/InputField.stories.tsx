import type { Meta, StoryObj } from "@storybook/react";
import { InputField } from "./InputField";

const meta: Meta<typeof InputField> = {
  title: "Atoms/Input",
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
  args: {
    placeholder: "Planet of Residency",
    value: "",
  },
};
