
import React, { useState } from "react";
import { ProfileCard } from "./ProfileCard";
import type { Meta, StoryObj } from "@storybook/react";

const meta: Meta<typeof ProfileCard> = {
  title: "Organisms/ProfileCard",
  component: ProfileCard,
  tags: ["autodocs"],
};

export default meta;

type Story = StoryObj<typeof ProfileCard>;

export const Default: Story = {
  render: () => {
    const [profile, setProfile] = useState({
      name: "Zorg",
      age: "230",
      bodyform: "Slime-based",
      lookingfor: "Carbon-based companions",
      planet: "Nebulon 5",
    });

    return <ProfileCard profile={profile} setProfile={setProfile} />;
  },
};
