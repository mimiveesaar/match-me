import type { Meta, StoryObj } from "@storybook/nextjs";
import { PhotoBubble } from "./PhotoBubble";

const meta: Meta<typeof PhotoBubble> = {
    title: "@atoms/ChatSpace/Connections Menu/Profile Photo Bubble",
    component: PhotoBubble,
    argTypes: {
        src: { control: "text" },
        alt: { control: "text" },
    },
};

export default meta;

type Story = StoryObj<typeof PhotoBubble>;

export const WithImage: Story = {

    args: {
        src: "default-profile.png",
        alt: "User profile picture",
    },
    decorators: [
        (Story) => (
            <div style={{ width: "48px", height: "48px" }}>
                <Story />
            </div>
        ),
    ],
};