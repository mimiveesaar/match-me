import { Meta, StoryObj } from '@storybook/nextjs';
import { ConnectionsMenu } from './ConnectionsMenu';

const meta: Meta<typeof ConnectionsMenu> = {
  title: 'Organisms/ChatSpace/Connections Menu/Connections Menu',
  component: ConnectionsMenu,
  argTypes: {
    src: { control: 'text' },
    alt: { control: 'text' },
    username: { control: 'text' },
  },
};

export default meta;

type Story = StoryObj<typeof ConnectionsMenu>;

export const Default: Story = {
  args: {
    src: 'default-profile.png',
    alt: 'User profile picture',
    username: 'Shelly',
  },
  parameters: {
    layout: 'centered',
  },
};