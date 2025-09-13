import type { Meta, StoryObj } from "@storybook/nextjs";
import { SectionLine } from './SectionLine';

const meta: Meta<typeof SectionLine> = {
  title: "Atoms/Connections/SectionLine",
  component: SectionLine,
};
export default meta;

type Story = StoryObj<typeof SectionLine>;

export const Default: Story = {
  render: () => <div className="h-screen"><SectionLine /></div>,
};