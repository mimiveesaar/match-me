/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
    '../../packages/components/src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: 'var(--color-primary)',
        secondary: 'var(--color-secondary)',
        'secondary-dark': 'var(--color-secondary-dark)',
        'accent-light': 'var(--color-accent-light)',
        'accent-dark': 'var(--color-accent-dark)',
        dark: 'var(--color-dark)',
        darker: 'var(--color-darker)',
        olive: 'var(--color-olive)',
        amberglow: 'var(--color-amberglow)',
        peony: 'var(--color-peony)',
        minty: 'var(--color-minty)',
        moss: 'var(--color-moss)',
        coral: 'var(--color-coral)',
        limeburst: 'var(--color-limeburst)',
        ivory: 'var(--color-ivory)',
        'powder-blue': 'var(--color-powder-blue)',
        'ivory-90': 'var(--color-ivory-90)',
      },
      fontFamily: {
        sans: ['var(--font-ibm-plex-sans)', 'IBM Plex Sans', 'sans-serif'],
      },
    },
  },
  plugins: [],
}