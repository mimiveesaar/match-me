import type { Meta, StoryObj } from "@storybook/react";
import { LabeledMultiLineInput } from "./LabeledMultiLineInput";

const meta: Meta<typeof LabeledMultiLineInput> = {
  title: "Molecules/LabeledMultiLineInput",
  component: LabeledMultiLineInput,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof LabeledMultiLineInput>;

export const Default: Story = {
  args: {
    placeholder: "Bio (optional)",
    value: "",
    maxLength: 250,
  },
};
