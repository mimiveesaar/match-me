import { MatchCardBackground } from "./match_card_background";

export default {
  title: "Atoms/Match Cards/MatchCardBackground",
  component: MatchCardBackground,
};

const Template = (args) => <MatchCardBackground {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: <p>This is inside the card background.</p>,
};