/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx}',
    './pages/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',
    './packages/**/*.{js,ts,jsx,tsx}',         // Catches most packages
    './packages/components/src/**/*.{js,ts,jsx,tsx}', // Very specific
  ],
  safelist: [
    'border',
    'border-green-500',
    'border-black/70',
    'bg-transparent',
    'text-opacity-80',
    'rounded-custom-16',  // If this is a custom class
    'w-[265px]',
    'h-[196px]',
    'top-1',
    'right-1',
    'absolute',
    // Add more if needed
  ],
  theme: {
    extend: {},
  },
  plugins: [],
};