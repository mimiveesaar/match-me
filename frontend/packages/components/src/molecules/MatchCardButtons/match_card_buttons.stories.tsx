import { Meta, StoryObj } from "@storybook/react";
import { ButtonTriangle } from "./match_card_buttons";

const meta: Meta<typeof ButtonTriangle> = {
  title: "Molecules/ButtonTriangle",
  component: ButtonTriangle,
  parameters: {
    layout: "centered", // center the component in the Storybook preview
  },
};

export default meta;
type Story = StoryObj<typeof ButtonTriangle>;

export const Default: Story = {
  render: () => <ButtonTriangle />,
};