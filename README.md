
# Simple Microservices App using Spring Boot

A simple microservice application using Java Spring Boot


## Run Locally

Copy `docker-compose.yml-example` to `docker-compose.yml`

Build docker image

```bash
  mvn clean compile jib:dockerBuild
```

Build docker image and push to Dockerhub

```bash
  mvn clean compile jib:build
```

Build and run docker image

```bash
  docker compose up -d
```

add this to hosts in order to access "keycloak" host directly
```bash
  127.0.0.1       keycloak
```
## API Reference

#### Base URL API

```http
localhost:8080
```

#### Eureka Dashboard

```http
localhost:8080/eureka/dashboard
```

#### Keycloak URL

```http
  localhost:8081
```

#### Keycloak User and Password
```code
username: admin
password: admin
```