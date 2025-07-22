import type { Meta, StoryObj } from '@storybook/nextjs';
import { Menu } from './menu';

const meta: Meta<typeof Menu> = {
  title: 'Organisms/Menu/Menu',
  component: Menu,
};
export default meta;

type Story = StoryObj<typeof Menu>;

export const Default: Story = {
  render: () => <Menu />,
};