import type { Meta, StoryObj } from "@storybook/nextjs";
import { MatchCardProfilePic } from "./match_card_profile_pic";

const meta: Meta<typeof MatchCardProfilePic> = {
  title: "Atoms/Match Cards/Profile Picture",
  component: MatchCardProfilePic,
  argTypes: {
    src: { control: "text" },
    alt: { control: "text" },
    className: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof MatchCardProfilePic>;

export const WithImage: Story = {
  args: {
    src: "default-profile.png",
    alt: "User profile picture",
    className: "",
  },
  parameters: {
    layout: "centered", //Center in the storybook view
  },
};