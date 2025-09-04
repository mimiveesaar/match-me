import { Meta, StoryObj } from "@storybook/nextjs";
import { SendButton } from "./Button";

const meta: Meta<typeof SendButton> = {
  title: "Atoms/CHATSPACE/Message Input/Send Button",
  component: SendButton,
};

export default meta;

type Story = StoryObj<typeof SendButton>;

export const Default: Story = {
  render: (args) => <SendButton {...args} />,
  args: {
    onClick: () => alert("Send clicked!"),
  },
};