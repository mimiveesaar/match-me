import type { Meta, StoryObj } from "@storybook/nextjs";
import { PageLink } from "./Pagelink";

const meta: Meta<typeof PageLink> = {
    title: "Atoms/Menu/PageLink",
    component: PageLink,
};
export default meta;

type Story = StoryObj<typeof PageLink>;

export const Default: Story = {
    render: () => <PageLink label="Matches" />,
};