import { Meta, StoryObj } from "@storybook/nextjs";
import { MyConnectionsHeader } from "./MyConnectionsHeader";

const meta: Meta<typeof MyConnectionsHeader> = {
    title: "Molecules/Connections/My Connections Header",
    component: MyConnectionsHeader,
    parameters: {
        layout: "centered",
    },
};
export default meta;

type Story = StoryObj<typeof MyConnectionsHeader>

export const Default: Story = {
    render: () => <MyConnectionsHeader />,
};