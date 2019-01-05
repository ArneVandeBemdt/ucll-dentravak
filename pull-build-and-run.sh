#!/bin/bash

# a simple script to pull the latest version from the repo,
# build it with Gradle and run it with Docker

git pull
./gradlew jibDockerBuild
docker-compose-up
