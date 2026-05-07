# ---------- Build stage ----------
FROM oven/bun:1 AS builder
WORKDIR /app

COPY package.json bun.lock* bunfig.toml* ./
RUN bun install --frozen-lockfile || bun install

COPY . .
RUN bun run build

# ---------- Runtime stage (static SPA on port 3000) ----------
FROM nginx:alpine AS runner

# SPA fallback + listen on 3000
RUN printf 'server {\n\
  listen 3000;\n\
  server_name _;\n\
  root /usr/share/nginx/html;\n\
  index index.html;\n\
  location / {\n\
    try_files $uri $uri/ /index.html;\n\
  }\n\
}\n' > /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist/client /usr/share/nginx/html
RUN cp /usr/share/nginx/html/index.html /usr/share/nginx/html/404.html

EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]