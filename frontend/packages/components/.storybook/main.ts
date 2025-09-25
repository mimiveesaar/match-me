import type { StorybookConfig } from '@storybook/nextjs-vite';
import { createRequire } from 'module';
import tsconfigPaths from 'vite-tsconfig-paths';
import { mergeConfig } from 'vite';

import { join, dirname } from "path"
const require = createRequire(import.meta.url);
/**
* This function is used to resolve the absolute path of a package.
* It is needed in projects that use Yarn PnP or are set up within a monorepo.
*/
function getAbsolutePath(value: string): any {
  return dirname(require.resolve(join(value, 'package.json')))
}
const config: StorybookConfig = {
  "stories": [
    "../src/**/*.mdx",
    "../src/**/*.stories.@(js|jsx|ts|tsx|mdx)"
  ],
  "addons": [getAbsolutePath("@storybook/addon-docs")],
  "framework": {
    "name": getAbsolutePath('@storybook/nextjs-vite'),
    "options": {}
  },
  // "staticDirs": [
  //   "../public"
  // ],
  viteFinal: async (config, { configType }) => {
    return mergeConfig(config, {
      plugins: [tsconfigPaths({ projects: ["./tsconfig.json"] })],
    });
  }
};
export default config;