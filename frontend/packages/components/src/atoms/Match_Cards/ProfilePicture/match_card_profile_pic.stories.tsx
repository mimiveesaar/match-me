import { MatchCardProfilePic } from "./match_card_profile_pic";

export default {
  title: "Atoms/Match Cards/Profile Picture",
  component: MatchCardProfilePic,
};

export const WithImage = () => (
  <MatchCardProfilePic src="default-profile.png" />
);

export const WithoutImage = () => (
  <MatchCardProfilePic/>
);