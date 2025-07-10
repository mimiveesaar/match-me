import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';
import { DropdownSelector, DropdownSelectorProps } from './dropdown_selector';

const meta: Meta<typeof DropdownSelector> = {
  title: 'Atoms/Menu/Dropdown Selector',
  component: DropdownSelector,
  argTypes: {
    header: {control: 'text' },
    options: { control: 'array' },
    selectedOptions: { control: 'array' },
    onSelect: { action: 'selected' },
  },
};
export default meta;

type Story = StoryObj<typeof DropdownSelector>;

export const Default: Story = {
  args: {
    header: 'Choose option',
    options: ['Option 1', 'Option 2', 'Option 3'],
    selectedOptions: [],
  },
};

export const Interactive: Story = {
  render: (args: DropdownSelectorProps) => {
    const [selected, setSelected] = React.useState<string[]>(args.selectedOptions || []);

    const handleSelect = (option: string) => {
      setSelected((prev) =>
        prev.includes(option)
          ? prev.filter((item) => item !== option)
          : [...prev, option]
      );
    };

    return (
      <DropdownSelector
        {...args}
        selectedOptions={selected}
        onSelect={handleSelect}
      />
    );
  },
  args: {
    header: 'looking for',
    options: ['Friendship', 'Dating', 'Networking', 'Planetary playmate', 'Intergalactic romance'],
    selectedOptions: [],
  },
  parameters: {
    layout: 'centered',
  },
};

