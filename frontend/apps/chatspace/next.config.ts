import type { NextConfig } from "next";

const path = require('path');

const nextConfig: NextConfig = {
  
  webpack: (config) => {
    config.resolve.alias['@components-public'] = path.resolve(__dirname, '../../packages/components/public');
    return config;
  },
};

export default nextConfig;
