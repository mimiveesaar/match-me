import { Meta, StoryObj } from "@storybook/nextjs";
import { SpeechBubble } from "./SpeechBubble";


const meta: Meta<typeof SpeechBubble> = {
    title: "Atoms/ChatSpace/Chat Window/Speech Bubble",
    component: SpeechBubble,
};

export default meta;

type Story = StoryObj<typeof SpeechBubble>;

export const Default: Story = {
    render: (args) => (
        <div className="flex justify-center items-center w-[100vw] h-[100vh] bg-gray-100">
            <SpeechBubble {...args} />
        </div>
    ),
    args: {
        color: "bg-amberglow",
        children: (
            <div className="flex justify-center items-center w-[257px] h-[100px] text-lg">
                Speech Bubble
            </div>
        ),
    },
    parameters: {
        layout: "fullscreen",
    },
};