import type { Meta, StoryObj } from "@storybook/nextjs";
import { KeyIcon } from "./KeyIcon";
import { fn } from "storybook/test";

const meta: Meta<typeof KeyIcon> = {
  title: "Atoms/KeyIcon",
  component: KeyIcon,
  args: {
    onClick: fn(),
  },
};
export default meta;

export const Default: StoryObj<typeof meta> = {
  render: (args) => <KeyIcon {...args} />,
};
