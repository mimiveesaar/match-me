import type { NextConfig } from "next";
import path from "path";

const nextConfig: NextConfig = {
    webpack: (config: any) => {
        config.resolve.alias = {
            ...(config.resolve.alias || {}),
            '@atoms': path.resolve(__dirname, '../../packages/components/src/atoms'),
            '@molecules': path.resolve(__dirname, '../../packages/components/src/molecules'),
            '@organisms': path.resolve(__dirname, '../../packages/components/src/organisms'),
            '@templates': path.resolve(__dirname, '../../packages/components/src/templates'),
        };
        return config;
    },
};

export default nextConfig;