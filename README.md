<h1 align="left">React Spring JWT Oath2</h1>

###
Set the host mysite.com on your machine. Ex: `127.0.0.1 mysite.com`
</br>
**Services are sensitive to file changes.** Java projects need an IDE with an autocompiler for Spring DevTools to capture changes.
</br>
`Command to run: docker compose up`
</br>
User: email - admin@example.com password - 12345

###

# System Services

## Database
**Host:** `localhost:3306`

- **Stack:** MySQL
- **Service Name:** `mysql`
- **Responsibility:** Persist all application data.

---

## Proxy
**Host:** `localhost:80`

- **Stack:** Nginx
- **Responsibility:** Responsible for serving the frontend and redirecting other requests.

---

## Authentication
**Host:** `localhost:9000`

- **Stack:** Spring Boot
- **Service Name:** `authentication`
- **Responsibility:** Creates and populates the database. Issues JWT and OAuth2 tokens.

---

## Resource
**Host:** `localhost:3000`

- **Stack:** Spring Boot
- **Service Name:** `resource`
- **Responsibility:** —

---

## Client
**Host:** `localhost:3000`

- **Stack:** Spring Boot
- **Service Name:** `client`
- **Responsibility:** Retains the authentication token and controls the requests.

---

## Gateway
**Host:** `localhost:8080`

- **Stack:** Spring Boot
- **Service Name:** `gateway`
- **Responsibility:** Load balancer.

---

## Discovery Service
**Host:** `localhost:9090`

- **Stack:** Spring Boot
- **Service Name:** `discovery-service`
- **Responsibility:** Services register for load balancing.

---

## Front-end
**Host:** `localhost:5173`

- **Stack:** React JS
- **Service Name:** `frontend`
- **Responsibility:** Responsible for customer visualization.

###

<div align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="40" alt="spring logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" height="40" alt="docker logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nginx/nginx-original.svg" height="40" alt="nginx logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg" height="40" alt="react logo"  />
</div>

###
