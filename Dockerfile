# Minimal Dockerfile for Render deployment of this repository
# This repository is an Android app; Render cannot build/run Android directly.
# We provide a simple static page so Render Docker build succeeds.

FROM nginx:alpine

# Replace default nginx content with a tiny static page
RUN rm -rf /usr/share/nginx/html/*
COPY index.html /usr/share/nginx/html/index.html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
