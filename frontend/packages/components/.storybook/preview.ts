import type { Preview } from '@storybook/nextjs'
import '../public/style/styles.css';


const preview: Preview = {
  parameters: {
    actions : { argTypesRegex: "^on.*" },
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },
};

export default preview;