FROM node:22.11-alpine AS build
WORKDIR /src
# Yarn 4 comes from Corepack at the version pinned in package.json "packageManager" (TD-15).
ENV COREPACK_ENABLE_DOWNLOAD_PROMPT=0
RUN corepack enable
COPY package.json yarn.lock .yarnrc.yml ./
RUN yarn install --immutable
COPY . .
RUN yarn build

# Official NGINX image, stable line, run as its non-root nginx user (uid 101) on port 8080 (TD-02).
# apk upgrade pulls Alpine security fixes that the base image has not picked up yet.
FROM nginx:1.30.8-alpine
RUN apk upgrade --no-cache \
    && sed -i 's#^pid .*#pid /tmp/nginx.pid;#' /etc/nginx/nginx.conf \
    && chown -R nginx:nginx /var/cache/nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /src/dist /usr/share/nginx/html
USER 101
