FROM nginx:latest

COPY default.conf.template /etc/nginx/templates/default.conf.template

EXPOSE ${NGINX_PORT}

CMD ["nginx", "-g", "daemon off;"]