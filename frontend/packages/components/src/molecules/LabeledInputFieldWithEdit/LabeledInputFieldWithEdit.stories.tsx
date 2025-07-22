import type { Meta, StoryObj } from "@storybook/nextjs";
import { LabeledInputFieldWithEdit } from "./LabeledInputFieldWithEdit";

const meta: Meta<typeof LabeledInputFieldWithEdit> = {
  title: "Molecules/LabeledInputFieldWithEdit",
  component: LabeledInputFieldWithEdit,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof LabeledInputFieldWithEdit>;

export const Default: Story = {
  args: {
    label: "Planet",
    placeholder: "Planet of Residency",
    value: "",
  },
};
