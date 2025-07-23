import type { Meta, StoryObj } from "@storybook/nextjs";
import { OutgoingPingIcon } from "./OutgoingPingIcon";

const meta: Meta<typeof OutgoingPingIcon> = {
  title: "Atoms/Connections/Outgoing Ping Icon",
  component: OutgoingPingIcon,
};
export default meta;

type Story = StoryObj<typeof OutgoingPingIcon>;

export const Default: Story = {
  render: () => <OutgoingPingIcon />,
};
