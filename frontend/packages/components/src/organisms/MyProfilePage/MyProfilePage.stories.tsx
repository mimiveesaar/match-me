import type { Meta, StoryObj } from "@storybook/nextjs";
import { MyProfilePage } from "./MyProfilePage";

const meta: Meta<typeof MyProfilePage> = {
  title: "Organisms/MyProfilePage",
  component: MyProfilePage,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof MyProfilePage>;

export const Default: Story = {};
