import type { Meta, StoryObj } from "@storybook/nextjs";
import { MyConnectionsIcon } from "./MyConnectionsIcon";

const meta: Meta<typeof MyConnectionsIcon> = {
  title: "Atoms/Connections/My Connections Icon",
  component: MyConnectionsIcon,
};
export default meta;

type Story = StoryObj<typeof MyConnectionsIcon>;

export const Default: Story = {
  render: () => <MyConnectionsIcon />,
};
