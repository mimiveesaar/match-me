import type { Meta, StoryObj } from "@storybook/nextjs";
import { MenuBase } from './menu_base';

const meta: Meta<typeof MenuBase> = {
  title: "Atoms/Menu/Menu Base",
  component: MenuBase,
};
export default meta;

type Story = StoryObj<typeof MenuBase>;

export const Default: Story = {
  render: () => <MenuBase children={undefined} />,
};