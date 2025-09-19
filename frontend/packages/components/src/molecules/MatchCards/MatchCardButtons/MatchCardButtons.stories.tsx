import { Meta, StoryObj } from "@storybook/nextjs";
import { ButtonTriangle } from "./MatchCardButtons";

const meta: Meta<typeof ButtonTriangle> = {
  title: "Molecules/Match Cards/ButtonTriangle",
  component: ButtonTriangle,
  parameters: {
    layout: "centered",
  },
};
export default meta;

type Story = StoryObj<typeof ButtonTriangle>;

export const Default: Story = {
  render: () => <ButtonTriangle rejectedId={""} onReject={function(rejectedId: string): void {
        throw new Error("Function not implemented.");
    } } requestedId={""} onConnectionRequest={function(connectedId: string): void {
        throw new Error("Function not implemented.");
    } } />,
};