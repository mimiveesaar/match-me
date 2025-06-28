import type { Meta, StoryObj } from '@storybook/react';
import { BioTextbox } from './biobox';

const meta: Meta<typeof BioTextbox> = {
  title: 'Atoms/Match Cards/Bio/BioTextbox',
  component: BioTextbox,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof BioTextbox>;

export const Default: Story = {
  args: {
    bio: `Hi, I'm Shelly!
Martian explorer by day, dream weaver by night.
Lover of olive lattes and quantum poetry.`,
  },
};

export const ShortBio: Story = {
  args: {
    bio: 'Just vibes from Saturn.',
  },
};

export const LongBio: Story = {
  args: {
    bio: `Cosmic being. Stargazer. Collector of planetary trinkets.
I spend my time hopping comets and listening to the hum of galaxies.
Ask me about wormholes and poetry.`,
  },
};