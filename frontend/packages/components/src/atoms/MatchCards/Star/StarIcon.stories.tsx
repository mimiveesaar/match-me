import { Meta, StoryObj } from '@storybook/nextjs';
import { StarIcon } from "./StarIcon";

const meta: Meta<typeof StarIcon> = {
  title: 'Atoms/Match Cards/Star Icon',
  component: StarIcon,
};

export default meta;

type Story = StoryObj<typeof StarIcon>;

export const Default: Story = {
  render: () => <StarIcon />,
};