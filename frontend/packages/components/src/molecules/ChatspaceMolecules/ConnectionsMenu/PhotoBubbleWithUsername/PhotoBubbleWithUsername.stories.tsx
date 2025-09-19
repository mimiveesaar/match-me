import { Meta, StoryObj } from '@storybook/nextjs';
import { PhotoBubbleWithUsername } from "./PhotoBubbleWithUsername";


const meta: Meta<typeof PhotoBubbleWithUsername> = {
  title: 'Molecules/ChatSpace/Connections Menu/Photo Bubble With Username',
  component: PhotoBubbleWithUsername,
  argTypes: {
    src: { control: 'text' },
    alt: { control: 'text' },
    username: { control: 'text' },
  },
};

export default meta;

type Story = StoryObj<typeof PhotoBubbleWithUsername>;

export const Default: Story = {
  args: {
    src: 'default-profile.png',
    alt: 'User profile picture',
    username: 'Shelly',
  },
  decorators: [
    (Story) => (
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
        <Story />
      </div>
    ),
  ],
};