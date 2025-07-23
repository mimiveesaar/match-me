import type { Meta, StoryObj } from "@storybook/nextjs";
import { AcceptButton } from './AcceptButton';

const meta: Meta<typeof AcceptButton> = {
  title: "Atoms/Connections/Accept Button",
  component: AcceptButton,
};
export default meta;

type Story = StoryObj<typeof AcceptButton>;

export const Default: Story = {
  render: () => <AcceptButton />,
};