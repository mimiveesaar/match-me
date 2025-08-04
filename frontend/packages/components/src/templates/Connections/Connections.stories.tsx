import { Meta, StoryObj } from "@storybook/nextjs";
import { Connections } from "./Connections";

const meta: Meta<typeof Connections> = {
  title: "Templates/Connections",
  component: Connections,
  argTypes: {
    incomingPings: {
      control: {
        type: "object",
      },
    },
    outgoingPings: {
      control: {
        type: "object",
      },
    },
    myConnections: {
      control: {
        type: "object",
      },
    },
  },
  args: {
    incomingPings: {
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
        {
          id: "3",
          username: "Charlie",
          profilePictureUrl:
            "https://plus.unsplash.com/premium_photo-1739036236186-cf7aac34ffc8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        },
        {
          id: "4",
          username: "Dave",
          profilePictureUrl:
            "https://images.unsplash.com/photo-1607337202714-a88f7abbdee7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YWxpZW58ZW58MHx8MHx8fDA%3D",
        },
        {
          id: "5",
          username: "Eve",
          profilePictureUrl:
            "https://plus.unsplash.com/premium_photo-1739036236186-cf7aac34ffc8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        },
        {
          id: "6",
          username: "Frank",
          profilePictureUrl:
            "https://images.unsplash.com/photo-1607337202714-a88f7abbdee7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YWxpZW58ZW58MHx8MHx8fDA%3D",
        },
        {
          id: "7",
          username: "Grace",
          profilePictureUrl:
            "https://plus.unsplash.com/premium_photo-1739036236186-cf7aac34ffc8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        },
        {
          id: "8",
          username: "Heidi",
          profilePictureUrl:
            "https://images.unsplash.com/photo-1607337202714-a88f7abbdee7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YWxpZW58ZW58MHx8MHx8fDA%3D",
        },
      ],
    },
    outgoingPings: {
      outgoingPings: [
        {
          id: "1",
          username: "Charlie",
          profilePictureUrl:
            "https://images.unsplash.com/photo-1502685104226-9c5b5c5b5c5b?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Y2hhcmxpZXxlbnwwfHwwfHw%3D&w=600&q=60",
        },
      ],
    },
    myConnections: {
      myConnections: [
        {
          id: "1",
          username: "Dave",
          profilePictureUrl:
            "https://images.unsplash.com/photo-1502685104226-9c5b5c5b5c5b?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Y2hhcmxpZXxlbnwwfHwwfHw%3D&w=600&q=60",
        },
        {
          id: "2",
          username: "Eve",
          profilePictureUrl:
            "https://images.unsplash.com/photo-1607337202714-a88f7abbdee7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YWxpZW58ZW58MHx8MHx8fDA%3D",
        },
      ],
    },
  },
};
export default meta;

type Story = StoryObj<typeof Connections>;

export const Default: Story = {
  render: (props) => (
    <div className="flex h-screen w-full items-center justify-center">
      <Connections {...props} />
    </div>
  ),
};
