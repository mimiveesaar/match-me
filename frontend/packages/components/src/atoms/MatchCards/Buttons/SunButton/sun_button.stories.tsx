import type { Meta, StoryObj } from "@storybook/nextjs";
import { SunButton } from './sun_button';

const meta: Meta<typeof SunButton> = {
  title: "Atoms/Match Cards/Sun Button",
  component: SunButton,
};
export default meta;

type Story = StoryObj<typeof SunButton>;

export const Default: Story = {
  render: () => <SunButton />,
};