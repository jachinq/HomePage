import { defineConfig } from '@rsbuild/core';
import { pluginVue } from '@rsbuild/plugin-vue';
import { describe } from 'node:test';

export default defineConfig({
  mode: process.env.NODE_ENV === 'production' ? 'production' : 'development',
  plugins: [pluginVue()],
  html: {
    title: 'Home Page',
    favicon: './src/assets/favicon.ico',
    meta: {
      description: 'My Personal Home Page and App Entry',
    }
  },
  resolve: {
    alias: {
      '@': './src',
      '@components': './src/components',
      '@views': './src/views',
      '@assets': './src/assets',
      '@api': './src/api',
    },
  },
  dev: {
    writeToDisk: false,
  },
  output: {
    distPath: {
      root: '../resources/static'
    }
  }
});
