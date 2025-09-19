import type { Meta, StoryObj } from "@storybook/nextjs";
import { MenuBase } from "./MenuBase";


const meta: Meta<typeof MenuBase> = {
  title: "Atoms/Menu/Menu Base",
  component: MenuBase,
};
export default meta;

type Story = StoryObj<typeof MenuBase>;

export const Default: Story = {
  render: () => (
    <div className="flex justify-center items-center h-screen">
      <MenuBase children={undefined} />
    </div>
  ),
};