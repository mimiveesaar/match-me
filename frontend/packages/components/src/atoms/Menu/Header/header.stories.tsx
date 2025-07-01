import type { Meta, StoryObj } from "@storybook/react";
import { MenuHeader } from './header';

const meta: Meta<typeof MenuHeader> = {
  title: "Atoms/Menu/Header",
  component: MenuHeader,
};

export default meta;

type Story = StoryObj<typeof MenuHeader>;

export const Default: Story = {
  render: () => <MenuHeader header={"Menu"} />,
};