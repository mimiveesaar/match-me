import React from "react";
import { ProfileCard } from "./ProfileCard";
import type { Meta, StoryObj } from "@storybook/react";

const meta: Meta<typeof ProfileCard> = {
  title: "Organisms/ProfileCard",
  component: ProfileCard,
  tags: ["autodocs"],
};

export default meta;

type Story = StoryObj<typeof ProfileCard>;

export const Default: Story = {};
