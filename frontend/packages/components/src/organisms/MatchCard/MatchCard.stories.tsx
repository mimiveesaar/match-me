import { MatchCard } from "./MatchCard";

export default {
  title: "Organisms/Match Card/Profile",
  component: MatchCard,
};

export const Default = () => (
  <MatchCard profilePicSrc="/images/example-user.png" cardColor="coral" />
);