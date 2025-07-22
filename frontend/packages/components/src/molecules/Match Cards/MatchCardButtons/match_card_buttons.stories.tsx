import { Meta, StoryObj } from "@storybook/nextjs";
import { ButtonTriangle } from "./match_card_buttons";

const meta: Meta<typeof ButtonTriangle> = {
  title: "Molecules/Match Cards/ButtonTriangle",
  component: ButtonTriangle,
  parameters: {
    layout: "centered",
  },
};
export default meta;

type Story = StoryObj<typeof ButtonTriangle>;

export const Default: Story = {
  render: () => <ButtonTriangle />,
};