import { Meta, StoryObj } from "@storybook/nextjs";
import { IncomingPingsHeader } from "./IncomingPingsHeader";

const meta: Meta<typeof IncomingPingsHeader> = {
    title: "Molecules/Connections/Incoming Pings Header",
    component: IncomingPingsHeader,
    parameters: {
        layout: "centered",
    },
};
export default meta;

type Story = StoryObj<typeof IncomingPingsHeader>

export const Default: Story = {
    render: () => <IncomingPingsHeader />,
};