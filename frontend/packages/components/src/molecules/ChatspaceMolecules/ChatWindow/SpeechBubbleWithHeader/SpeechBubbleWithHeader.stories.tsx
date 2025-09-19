import {Meta, StoryObj} from "@storybook/nextjs";
import { SpeechBubbleWithHeader } from "./SpeechBubbleWithHeader";

const meta: Meta<typeof SpeechBubbleWithHeader> = {
    title: "Molecules/ChatSpace/Chat Window/Speech Bubble With Header",
    component: SpeechBubbleWithHeader,
    parameters: {
        layout: "centered",
    },
};

export default meta;

type Story = StoryObj<typeof SpeechBubbleWithHeader>;

export const Default: Story = {
    render: (args) => (
        <div className="flex justify-center items-start w-[100vw] h-[100vh] bg-gray-100">
            <SpeechBubbleWithHeader {...args} />
        </div>
    ),
    args: {
        sender: "Zorbplat",
        time: "1:11 PM",
        date: "6/9/25",
        bubbleColor: "bg-amberglow",
        children: (
            <div className="flex justify-center items-center w-[257px] h-[100px] text-lg">
                Speech Bubble With Header
            </div>
        ),
    },
    parameters: {
        layout: "fullscreen",
    },
};