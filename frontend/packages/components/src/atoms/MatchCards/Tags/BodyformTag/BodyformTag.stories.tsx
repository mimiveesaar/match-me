import type { Meta, StoryObj } from "@storybook/nextjs";
import { BodyformTag } from "./BodyformTag";

const meta: Meta<typeof BodyformTag> = {
  title: "Atoms/MatchCards/BodyformTag",
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
