import type { Meta, StoryObj } from "@storybook/nextjs";
import {
  DeclineButton,
  DeclineButtonIvory,
  DeclineButtonRed,
} from "./DeclineButton";

const meta: Meta<typeof DeclineButton> = {
  title: "Atoms/Connections/Decline Button",
  component: DeclineButton,
};
export default meta;

type Story = StoryObj<typeof DeclineButton>;

export const Default: Story = {
  render: () => <DeclineButton />,
};

export const DeclineButtonPrimary: Story = {
  render: () => <DeclineButtonRed />,
};

export const DeclineButtonSecondary: Story = {
  render: () => <DeclineButtonIvory />,
};
