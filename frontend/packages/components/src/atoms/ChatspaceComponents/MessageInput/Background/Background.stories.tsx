import type { Meta, StoryObj } from "@storybook/nextjs";
import { Background } from './Background';

const meta: Meta<typeof Background> = {
    title: "Atoms/Chatspace/MessageInput/Background",
    component: Background,
};
export default meta;

type Story = StoryObj<typeof Background>;

export const Default: Story = {
  render: () => (
    <div className="flex justify-center items-center">
      <Background children={undefined} />
    </div>
  ),
};