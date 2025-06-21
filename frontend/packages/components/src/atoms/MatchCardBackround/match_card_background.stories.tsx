import { CardBackground } from "./match_card_background";

export default {
  title: "Atoms/CardBackground",
  component: CardBackground,
};

const Template = (args) => <CardBackground {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: <p>This is inside the card background.</p>,
};