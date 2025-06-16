import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import tsconfigPaths from 'vite-tsconfig-paths'
import { builtinModules } from 'module'
import pkg from './package.json'
import { visualizer } from "rollup-plugin-visualizer";
import dts from 'vite-plugin-dts'
import preserveDirectives from 'rollup-preserve-directives';


export default defineConfig({
  plugins: [react(), tailwindcss(), tsconfigPaths(), dts({tsconfigPath : './tsconfig.json'}), visualizer()],
  build : {
    ssr: true,
    outDir: 'dist',
    lib : {
      entry : './src/index.ts',
      name : 'Components',
      fileName : 'components',
      formats : ['es']
    },

    rollupOptions: {
      // Externalize deps that shouldn't be bundled
      external: [
          ...builtinModules,
        ...Object.keys(pkg.peerDependencies || {}),
      ],
      output: {
         exports: 'auto',
         preserveModules: true,
         preserveModulesRoot: 'src',
      },

      plugins : [
        preserveDirectives()
      ]
    },
  }
})
