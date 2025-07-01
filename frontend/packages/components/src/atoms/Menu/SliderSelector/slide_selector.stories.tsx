import type { Meta, StoryObj } from '@storybook/react';
import { SlideSelector } from './slide_selector';

const meta: Meta<typeof SlideSelector> = {
  title: 'Atoms/Menu/Slider Selector',
  component: SlideSelector,
};
export default meta;

type Story = StoryObj<typeof SlideSelector>;

export const Default: Story = {
  render: () => <SlideSelector header={''} minimum={0} maximum={10} />,
};  