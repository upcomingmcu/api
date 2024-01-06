# [This project has been moved to GitLab.](https://gitlab.com/upcomingmcu/api)

# UMCU API

[![Kotlin v1.8.22](https://img.shields.io/badge/Kotlin-v1.8.22-7a4ff3?logo=kotlin&logoColor=white)](https://github.com/JetBrains/kotlin/releases/tag/v1.8.22) [![Spring v3.1.5](https://img.shields.io/badge/Spring-v3.1.5-6aad3d?logo=spring&logoColor=white)](https://spring.io/blog/2023/10/19/spring-boot-3-1-5-available-now) [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/seanodev/umcu-api?logo=docker&logoColor=white&label=Docker%20Image)](https://hub.docker.com/r/seanodev/umcu-api)

[![Java CI with Gradle](https://github.com/upcomingmcu/api/actions/workflows/gradle.yml/badge.svg?branch=dev)](https://github.com/upcomingmcu/api/actions/workflows/gradle.yml) [![Docker Image CI](https://github.com/upcomingmcu/api/actions/workflows/docker-image.yml/badge.svg)](https://github.com/upcomingmcu/api/actions/workflows/docker-image.yml)

# About

"UMCU API" or "Upcoming MCU API" is a lightweight, standalone REST API to retrieve information about all past and future
productions within
the [Marvel Cinematic Universe (MCU)](https://en.wikipedia.org/wiki/Marvel_Cinematic_Universe).

This repo contains the entire source code for the API as well as instructions on how to host it yourself through Docker.
The official instance is located at https://api.umcu.app.

For documentation, visit https://api.umcu.app/swagger-ui/index.html.

# Installation

The recommended (and easiest) way to run this API is through Docker.

## Prerequisites

- Docker must be installed on your system.
- You must obtain a TMDB API Read Access Token (https://www.themoviedb.org/settings/api).

## Docker

This API has a prebuilt Docker image (https://hub.docker.com/r/seanodev/umcu-api) to quickly spin up a production ready
environment.

To deploy said image, you must run this image alongside a PostgreSQL image. For example, you can set that up like so in
Docker Compose file.

```yaml
version: "3.8"

services:
  db:
    image: postgres:16-bookworm
    # These environment variables must match the environment variables defined in the app service.
    environment:
      - POSTGRES_PASSWORD=ChangeMe
      - POSTGRES_USER=umcu
    expose:
      - 5432
    volumes:
      - ./db_data:/var/lib/postgresql/data
    restart: unless-stopped

  app:
    image: seanodev/umcu-api
    # These environment variables must match the environment variables defined in the db service.
    environment:
      - POSTGRES_PASSWORD=ChangeMe
      - POSTGRES_USER=umcu
      - TMDB_READ_ACCESS_TOKEN=...
    ports:
      - "8080:8080"
    restart: unless-stopped
    depends_on:
      - db
```

This configuration will spin up the database and app services so that the app can communicate with the database.

For a more detailed Docker Compose configuration (including a reverse proxy set up), please
see [compose-prod.yaml](compose-prod.yaml).

---

[This product uses the TMDB API but is not endorsed or certified by TMDB.](https://developer.themoviedb.org/docs)
