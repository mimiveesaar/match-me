import { Meta, StoryObj } from "@storybook/nextjs";

import { useState } from "react";
import { MessageInput } from "./MessageInput";

const meta: Meta<typeof MessageInput> = {
  title: "Organisms/CHATSPACE/Message Input",
  component: MessageInput,
};

export default meta;

type Story = StoryObj<typeof MessageInput>;

export const Default: Story = {
  render: () => {
    const [message, setMessage] = useState("");

    return (
      <div className="flex justify-center items-center w-screen h-screen bg-gray-100 p-8">
        <MessageInput
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onSend={(msg) => {
            alert(`Message sent: ${msg}`);
            setMessage("");
          }}
        />
      </div>
    );
  },
  parameters: {
    layout: "fullscreen",
  },
};