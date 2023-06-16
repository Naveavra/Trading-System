import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { frontPort } from './src/config'
import optimizer from 'vite-plugin-optimizer';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    optimizer({
      // path: () => ({
      //     find: /^path$/,
      //     code: `const path = require('path'); export { path as default }`,
      // }),
      // fs: () => ({
      //   // this is consistent with the `alias` behavior
      //   find: /^(node:)?fs$/,
      //   code: `const fs = require('fs'); export { fs as default }`
      // }),
    }),

  ],
  server: {
    port: frontPort,
  }
})
