import { Meta, StoryObj } from '@storybook/nextjs';
import { ConnectionsMenu } from "./ConnectionsMenu";

const meta: Meta<typeof ConnectionsMenu> = {
  title: 'Organisms/ChatSpace/Connections Menu/Connections Menu',
  component: ConnectionsMenu,

};

export default meta;

type Story = StoryObj<typeof ConnectionsMenu>;

export const Default: Story = {

  parameters: {
    layout: 'centered',
  },
};