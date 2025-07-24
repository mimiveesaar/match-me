import { Meta, StoryObj } from "@storybook/nextjs";
import { ConnectionProfile } from "./ConnectionProfile";

const meta: Meta<typeof ConnectionProfile> = {
  title: "Molecules/Connections/Connection Profile",
  component: ConnectionProfile,
  parameters: {
    layout: "centered",
  },
};
export default meta;

type Story = StoryObj<typeof ConnectionProfile>;

export const Default: Story = {
  render: () => (
    <div className="flex w-screen items-center justify-center">
      <ConnectionProfile />
    </div>
  ),
};
