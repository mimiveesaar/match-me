import type { Meta, StoryObj } from "@storybook/react";
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
  args: {
    placeholder: "Write your bio here...",
    value: "",
    maxLength: 250,
    rows: 4,
  },
};
