import type { Meta, StoryObj } from "@storybook/nextjs";
import { MobileMenu } from "./MobileMenu";

const meta: Meta<typeof MobileMenu> = {
    title: "Organisms/MobileMenu/MobileMenu",
    component: MobileMenu,
};

export default meta;

type Story = StoryObj<typeof MobileMenu>;

export const Default: Story = {
    render: () => <MobileMenu hasUnread={false} filters={undefined} setFilters={function(filters: any): void {
        throw new Error("Function not implemented.");
    } }/>,
};