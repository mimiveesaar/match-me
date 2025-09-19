import type { Meta, StoryObj } from "@storybook/nextjs";
import { Menu } from "./Menu";


const meta: Meta<typeof Menu> = {
    title: "Organisms/Menu/Menu",
    component: Menu,
};
export default meta;

type Story = StoryObj<typeof Menu>;

export const Default: Story = {
    render: () => <Menu hasUnread={false} />,
};

export const WithUnread: Story = {
    render: () => <Menu hasUnread={true} />,
};