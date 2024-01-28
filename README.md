
# Simple Microservices

A simple microservice application using Java Spring Boot


## Run Locally

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