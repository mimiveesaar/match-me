import { Meta, StoryObj } from "@storybook/nextjs";
import { OutgoingPingsHeader } from "./OutgoingPingsHeader";

const meta: Meta<typeof OutgoingPingsHeader> = {
  title: "Molecules/Connections/Outgoing Pings Header",
  component: OutgoingPingsHeader,
  parameters: {
    layout: "centered",
  },
};
export default meta;

type Story = StoryObj<typeof OutgoingPingsHeader>;

export const Default: Story = {
  render: () => <OutgoingPingsHeader />,
};
