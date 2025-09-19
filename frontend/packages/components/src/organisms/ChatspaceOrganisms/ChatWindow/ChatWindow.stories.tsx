import { Meta, StoryObj } from "@storybook/nextjs";
import { ChatWindow } from "./ChatWindow";

const meta: Meta<typeof ChatWindow> = {
  title: "Organisms/ChatSpace/ChatWindow",
  component: ChatWindow,
};

type Story = StoryObj<typeof ChatWindow>;

export default meta;

export const Default: Story = {
  render: () => (
    <div className="flex justify-center items-start w-[100vw] h-[100vh] bg-gray-100 p-8">
      <ChatWindow messages={[]} currentUserId={""} />
    </div>
  ),
  parameters: {
    layout: "fullscreen",
  },
};