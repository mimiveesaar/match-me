import type { Meta, StoryObj } from "@storybook/nextjs";
import { MenuHeader } from './Header';

const meta: Meta<typeof MenuHeader> = {
  title: "Atoms/Menu/Header",
  component: MenuHeader,
};

export default meta;

type Story = StoryObj<typeof MenuHeader>;

export const Default: Story = {
  render: () => <MenuHeader header={"Menu"} />,
};