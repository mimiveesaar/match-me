import type { Meta, StoryObj } from "@storybook/nextjs";
import { MoonButton } from './MoonButton';

const meta: Meta<typeof MoonButton> = {
  title: "Atoms/Match Cards/Moon Button",
  component: MoonButton,
};
export default meta;

type Story = StoryObj<typeof MoonButton>;

export const Default: Story = {
  render: () => <MoonButton />,
};