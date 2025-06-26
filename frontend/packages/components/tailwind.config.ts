
import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/**/*.{js,ts,jsx,tsx}',

  ],
  theme: {
    extend: {
      colors: {
        olive: '#DBDB72',
        amberglow: '#FDC167',
        peony: '#F6D8EC',
        minty: '#D2F0EA',
        moss: '#BCC5AA',
        coral: '#EF764E',
        limeburst: '#30F84E',
      },
    },
  },
  plugins: [],
}
export default config
