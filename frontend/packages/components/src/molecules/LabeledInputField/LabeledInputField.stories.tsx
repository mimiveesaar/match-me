import type { Meta, StoryObj } from "@storybook/react";
import { LabeledInputField } from "./LabeledInputField";

const meta: Meta<typeof LabeledInputField> = {
  title: "Molecules/LabeledInput",
  component: LabeledInputField,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof LabeledInputField>;

export const Default: Story = {
  args: {
    label: "Planet",
    placeholder: "Planet of Residency",
    value: "",
  },
};
