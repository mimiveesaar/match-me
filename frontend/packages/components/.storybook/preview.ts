import type { Preview } from '@storybook/nextjs'
import 'tailwindcss/tailwind.css';
import '../src/index.css'
import '../public/style/styles.css';

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
       color: /(background|color)$/i,
       date: /Date$/i,
      },
    },
  },
};

export default preview;