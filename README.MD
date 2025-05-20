# Corporate System — Java Web Application

A microservice-based system built with Java and Spring Boot. It includes several backend services that communicate through REST and RabbitMQ.

## Services

- **`user-service`** — user management (uses H2 with file-based persistence)
- **`time-tracking-service`** — working hours and activity tracking (uses MySQL)
- **`notification-service`** — sends notifications to users via RabbitMQ (uses PostgreSQL)
- **`telegram-bot`** — Telegram bot that receives events from RabbitMQ and sends reports (uses PostgreSQL)

## Tech Stack

- Java 17 + Spring Boot 3
- Docker + Kubernetes
- MySQL / PostgreSQL / H2
- RabbitMQ
- Feign Clients
- Liquibase
- JUnit 5

## Getting Started

###  Build local Docker images (without push)

```bash

chmod +x docker-build.sh
./docker-build.sh
```

###  Deploy to Kubernetes

```bash

chmod +x deploy.sh
./deploy.sh
```

### Stop and delete all Kubernetes resources

```bash

chmod +x delete.sh
./delete.sh
```

## Notes

- All services are isolated in their own Kubernetes namespaces.
- Configuration is managed via `ConfigMap` and `Secret` resources.
- Persistent storage is used for MySQL, PostgreSQL, and H2 (via PVCs).
- Services discover each other via DNS (e.g. `http://time-tracking-service.time-tracking-ns.svc.cluster.local`).



## Commands 
```bash

get pods --all-namespaces

```
 

