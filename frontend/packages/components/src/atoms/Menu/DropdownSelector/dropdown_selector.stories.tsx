import type { Meta, StoryObj } from '@storybook/react';
import { DropdownSelector } from './dropdown_selector';

const meta: Meta<typeof DropdownSelector> = {
  title: 'Atoms/Menu/Dropdown Selector',
  component: DropdownSelector,
  argTypes: {
    header: {control: 'text' },
    options: { control: 'array' },
    selectedOption: { control: 'text' },
    onSelect: { action: 'selected' },
  },
};
export default meta;

type Story = StoryObj<typeof DropdownSelector>;

export const Default: Story = {
  args: {
    header: ['looking for'],
    options: ['Option 1', 'Option 2', 'Option 3'],
    selectedOption: 'Option 2',
  },
  parameters: {
    layout: 'centered',
  },
};