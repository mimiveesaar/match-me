import type { Meta, StoryObj } from "@storybook/nextjs";
import { BodyformTag } from "./bodyform_tag";

const meta: Meta<typeof BodyformTag> = {
  title: "Atoms/Match Cards/Bodyform Tag",
  component: BodyformTag,
  argTypes: {
    bodyform: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof BodyformTag>;

export const Default: Story = {
  args: {
    bodyform: "Mars",
  },
  parameters: {
    layout: "centered", 
  },
};
