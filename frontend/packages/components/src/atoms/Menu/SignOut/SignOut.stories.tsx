import type { Meta, StoryObj } from '@storybook/nextjs';
import { SignOutButton } from "./SignOut";


const meta: Meta<typeof SignOutButton> = {
  title: 'Atoms/Menu/Sign Out',
  component: SignOutButton,
};  
export default meta;

type Story = StoryObj<typeof SignOutButton>;

export const Default: Story = {
  render: () => <SignOutButton />,
};