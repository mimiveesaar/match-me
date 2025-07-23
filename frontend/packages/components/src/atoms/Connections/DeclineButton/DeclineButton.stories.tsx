import type { Meta, StoryObj } from "@storybook/nextjs";
import { DeclineButton } from "./DeclineButton";

const meta: Meta<typeof DeclineButton> = {
  title: "Atoms/Connections/Decline Button",
  component: DeclineButton,
  argTypes: {
    className: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof DeclineButton>;

export const Default: Story = {
  render: (args) => <DeclineButton {...args} />,
  args: {
    className: "text-coral w-5 h-5 hover:text-red-700",
  },
};

export const White: Story = {
  render: (args) => <DeclineButton {...args} />,
  args: {
    className: "text-ivory w-5 h-5 hover:text-red-500",
  },
};
