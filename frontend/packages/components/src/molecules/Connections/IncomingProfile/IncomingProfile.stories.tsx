import { Meta, StoryObj } from "@storybook/nextjs";
import { IncomingProfile } from "./IncomingProfile";

const meta: Meta<typeof IncomingProfile> = {
  title: "Molecules/Connections/Incoming Profile",
  component: IncomingProfile,
  parameters: {
    layout: "centered",
  },
};
export default meta;

type Story = StoryObj<typeof IncomingProfile>;

export const Default: Story = {
  render: () => (
    <div className="flex w-screen items-center justify-center">
      <IncomingProfile />
    </div>
  ),
};
