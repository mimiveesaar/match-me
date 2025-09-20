import type { Meta, StoryObj } from "@storybook/nextjs";
import { MessageInputBackground } from './MessageInputBackground';

const meta: Meta<typeof MessageInputBackground> = {
    title: "Atoms/Chatspace/MessageInput/Background",
    component: MessageInputBackground,
};
export default meta;

type Story = StoryObj<typeof MessageInputBackground>;

export const Default: Story = {
  render: () => (
    <div className="flex justify-center items-center">
      <MessageInputBackground children={undefined} />
    </div>
  ),
};