import type { NextConfig } from "next";
import path from "path";

const nextConfig: NextConfig = {
  webpack(config) {
    config.resolve.alias = {
      ...(config.resolve.alias || {}),
      '@atoms': path.resolve(__dirname, '../../packages/components/src/atoms'),
      '@molecules': path.resolve(__dirname, '../../packages/components/src/molecules'),
    };
    return config;
  },
};

export default nextConfig;