import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // your Spring Boot port
        changeOrigin: true,               // hides that request came from different port
      }
    }
  }
})