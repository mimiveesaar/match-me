import { Meta, StoryObj } from "@storybook/nextjs";
import { MessageHeader } from "./MessageHeader";


const meta: Meta<typeof MessageHeader> = {
  title: "Atoms/ChatSpace/Chat Window/Message Header",
  component: MessageHeader,
};

export default meta;

type Story = StoryObj<typeof MessageHeader>;

export const Default: Story = {
  render: (args) => (
    <div className="flex justify-center items-center w-[100vw] h-[100vh] bg-gray-100">
        <MessageHeader {...args} />
        </div>
    ),
    args: {
        sender: "Zorbplat",
        time: "1:11 PM",
        date: "6/9/25",
    },
    parameters: {
        layout: "fullscreen", 
    },
};