import type { Meta, StoryObj } from "@storybook/nextjs";
import { IncomingPingIcon } from "./IncomingPingIcon";

const meta: Meta<typeof IncomingPingIcon> = {
  title: "Atoms/Connections/Incoming Ping Icon",
  component: IncomingPingIcon,
};
export default meta;

type Story = StoryObj<typeof IncomingPingIcon>;

export const Default: Story = {
  render: () => <IncomingPingIcon />,
};
