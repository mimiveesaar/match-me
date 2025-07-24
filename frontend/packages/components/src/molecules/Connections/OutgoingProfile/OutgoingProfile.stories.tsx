import { Meta, StoryObj } from "@storybook/nextjs";
import { OutgoingProfile } from "./OutgoingProfile";

const meta: Meta<typeof OutgoingProfile> = {
  title: "Molecules/Connections/Outgoing Profile",
  component: OutgoingProfile,
  parameters: {
    layout: "centered",
  },
};
export default meta;

type Story = StoryObj<typeof OutgoingProfile>;

export const Default: Story = {
  render: () => (
    <div className="flex w-screen items-center justify-center">
      <OutgoingProfile username="" profilePictureUrl="" />
    </div>
  ),
};
