import { Meta, StoryObj } from "@storybook/nextjs";
import { ConnectionsMenuBackground } from "./ConnectionsMenuBackground";

const meta: Meta<typeof ConnectionsMenuBackground> = {
  title: "Atoms/ChatSpace/ConnectionsMenu/Background",
  component: ConnectionsMenuBackground,
};

export default meta;

type Story = StoryObj<typeof ConnectionsMenuBackground>;

export const Default: Story = {
  render: (args) => (
    <div
      className="flex justify-center items-center w-[100vw] h-[100vh] bg-gray-100"
    >
      <ConnectionsMenuBackground {...args} />
    </div>
  ),
  args: {
    children: (
      <div className="flex justify-center items-center w-[204px] h-[650px] text-lg">
        Chat window background
      </div>
    ),
  },
  parameters: {
    layout: "fullscreen", 
  },
};