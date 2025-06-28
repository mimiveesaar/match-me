import { Meta, StoryObj } from '@storybook/react';
import { StarIcon } from './star_icon';

const meta: Meta<typeof StarIcon> = {
  title: 'Atoms/Match Cards/Star Icon',
  component: StarIcon,
};

export default meta;

type Story = StoryObj<typeof StarIcon>;

export const Default: Story = {
  render: () => <StarIcon />,
};