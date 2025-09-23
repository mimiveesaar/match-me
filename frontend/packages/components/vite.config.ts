import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import tsconfigPaths from "vite-tsconfig-paths";
import { builtinModules } from "module";
import dts from "vite-plugin-dts";
import preserveDirectives from "rollup-preserve-directives";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { storybookTest } from "@storybook/addon-vitest/vitest-plugin";
const dirname =
  typeof __dirname !== "undefined"
    ? __dirname
    : path.dirname(fileURLToPath(import.meta.url));

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    tsconfigPaths(),
    dts({
      tsconfigPath: "./tsconfig.json",
    }),
  ],
  build: {
    ssr: true,
    outDir: "dist",
    lib: {
      entry: [
        "./src/index.ts",
        "./src/atoms/index.ts",
        "./src/molecules/index.ts",
        "./src/organisms/index.ts",
        "./src/templates/index.ts",
      ],
      name: "Components",
      fileName: "components",
      formats: ["es"],
    },
    rollupOptions: {
      // Externalize deps that shouldn't be bundled
      external: [...builtinModules, 'react', 'react/jsx-runtime'],
      output: {
        exports: "auto",
        preserveModules: true,
        preserveModulesRoot: "src",
      },
      plugins: [preserveDirectives()],
    },
  },
  test: {
    projects: [
      {
        extends: true,
        plugins: [
          // The plugin will run tests for the stories defined in your Storybook config
          // See options at: https://storybook.js.org/docs/next/writing-tests/integrations/vitest-addon#storybooktest
          storybookTest({
            configDir: path.join(dirname, ".storybook"),
          }),
        ],
        test: {
          name: "storybook",
          browser: {
            enabled: true,
            headless: true,
            provider: "playwright",
            instances: [
              {
                browser: "chromium",
              },
            ],
          },
          setupFiles: [".storybook/vitest.setup.ts"],
        },
      },
    ],
  },
});
