import type { Meta, StoryObj } from "@storybook/nextjs";
import { IncomingPings } from "./IncomingPings";

const meta: Meta<typeof IncomingPings> = {
  title: "Organisms/Connections/Incoming Pings",
  component: IncomingPings,
  tags: ["autodocs"],
  argTypes: {
    incomingPings: {
        control: {
            type: "object",
        },
    }
  },
    args: {
    incomingPings: [
      {
        id: "1",
        username: "Alice",
        profilePictureUrl:
          "https://plus.unsplash.com/premium_photo-1739036236186-cf7aac34ffc8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      },
      {
        id: "2",
        username: "Bob",
        profilePictureUrl:
          "https://images.unsplash.com/photo-1607337202714-a88f7abbdee7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YWxpZW58ZW58MHx8MHx8fDA%3D",
      },
    ],
  },
};

export default meta;

type Story = StoryObj<typeof IncomingPings>;

export const Default: Story = {};


