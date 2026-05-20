import { defineConfig, loadEnv } from 'vite'
import react, { reactCompilerPreset } from '@vitejs/plugin-react'
import babel from '@rolldown/plugin-babel'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const ENV = loadEnv(mode, process.cwd(), '');

  return {
    server: {
      port: parseInt(ENV.VITE_PORT) || 5173,
      strictPort: true,
      allowedHosts: [
        'mysite.com'
      ]
    },
    plugins: [
      react(),
      babel({ presets: [reactCompilerPreset()] })
    ],
  };
});