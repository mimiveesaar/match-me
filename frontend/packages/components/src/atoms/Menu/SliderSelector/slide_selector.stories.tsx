import type { Meta, StoryObj } from '@storybook/nextjs';
import { RangeSlider } from './slide_selector';

const meta: Meta<typeof RangeSlider> = {
  title: 'Atoms/Menu/Slider Selector',
  component: RangeSlider,
};
export default meta;

type Story = StoryObj<typeof RangeSlider>;

export const Default: Story = {
  render: () => <RangeSlider />,
};  