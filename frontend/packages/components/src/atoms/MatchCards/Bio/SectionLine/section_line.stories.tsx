import type { Meta, StoryObj } from "@storybook/nextjs";
import { SectionLine } from './section_line';

const meta: Meta<typeof SectionLine> = {
  title: "Atoms/Match Cards/SectionLine",
  component: SectionLine,
};
export default meta;

type Story = StoryObj<typeof SectionLine>;

export const Default: Story = {
  render: () => <SectionLine />,
};