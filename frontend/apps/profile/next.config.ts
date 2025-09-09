/** @type {import('next').NextConfig} */
const path = require('path');

const nextConfig = {
  experimental: {
    transpilePackages: ['@your-org/components'], // Replace with your actual package name
  },
  webpack: (config: { resolve: { alias: any; }; }) => {
    config.resolve.alias = {
      ...config.resolve.alias,
      '@atoms': path.resolve(__dirname, '../../packages/components/src/atoms'),
      '@molecules': path.resolve(__dirname, '../../packages/components/src/molecules'),
      '@organisms': path.resolve(__dirname, '../../packages/components/src/organisms'),
      '@templates': path.resolve(__dirname, '../../packages/components/src/templates'),
    };
    return config;
  },
};

module.exports = nextConfig;