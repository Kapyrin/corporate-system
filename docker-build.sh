#!/bin/bash

set -e

echo "Building local Docker images..."

cd user-service
docker build -t user-service:latest .
cd ..

cd time-tracking-service
docker build -t time-tracking-service:latest .
cd ..

cd telegram-bot
docker build -t telegram-bot:latest .
cd ..

cd notification-service
docker build -t notification-service:latest .
cd ..

echo "All Docker images built locally."
