import { Meta, StoryObj } from "@storybook/nextjs";
import { Background } from "./Background";

const meta: Meta<typeof Background> = {
  title: "Atoms/ChatSpace/Chat Window/Background",
  component: Background,
};

export default meta;

type Story = StoryObj<typeof Background>;

export const Default: Story = {
  render: (args) => (
    <div
      className="flex justify-center items-center w-[100vw] h-[100vh] bg-gray-100"
    >
      <Background {...args} />
    </div>
  ),
  args: {
    children: (
      <div className="flex justify-center items-center w-[643px] h-[542px] text-lg">
        Chat window background
      </div>
    ),
  },
  parameters: {
    layout: "fullscreen", 
  },
};