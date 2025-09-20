import type { Meta, StoryObj } from "@storybook/nextjs";
import { BioSectionLine } from './BioSectionLine';

const meta: Meta<typeof BioSectionLine> = {
  title: "Atoms/Match Cards/SectionLine",
  component: BioSectionLine,
};
export default meta;

type Story = StoryObj<typeof BioSectionLine>;

export const Default: Story = {
  render: () => <BioSectionLine />,
};