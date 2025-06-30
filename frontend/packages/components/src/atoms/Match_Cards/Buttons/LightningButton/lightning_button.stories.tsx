import type { Meta, StoryObj } from "@storybook/react";
import { LightningButton } from './lightning_button';

const meta: Meta<typeof LightningButton> = {
  title: "Atoms/Match Cards/Lightning Button",
  component: LightningButton,
};
export default meta;

type Story = StoryObj<typeof LightningButton>;

export const Default: Story = {
  render: () => <LightningButton />,
};