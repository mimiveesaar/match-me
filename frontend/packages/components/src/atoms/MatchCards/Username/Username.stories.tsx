import type { Meta, StoryObj } from "@storybook/nextjs";
import { MatchCardsUsername } from "./MatchCardsUsername";

const meta: Meta<typeof MatchCardsUsername> = {
  title: "Atoms/MatchCards/Username",
  component: MatchCardsUsername,
  argTypes: {
    username: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof MatchCardsUsername>;


export const Default: Story = {
  args: {
    username: "Shelly",
  },
  parameters: {
    layout: "centered", 
  },
};