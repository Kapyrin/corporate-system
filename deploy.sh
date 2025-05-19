#!/bin/bash

echo "🔧 Creating namespaces..."
kubectl apply -f k8s/mysql/namespace.yaml
kubectl apply -f k8s/rabbitmq/namespace.yaml
kubectl apply -f k8s/postgres-telegram/namespace.yaml
kubectl apply -f k8s/postgres-notification/namespace.yaml
kubectl apply -f k8s/user-service/namespace.yaml
kubectl apply -f k8s/time-tracking-service/namespace.yaml
kubectl apply -f k8s/notification-service/namespace.yaml
kubectl apply -f k8s/telegram-bot/namespace.yaml

echo " Waiting 2s for namespaces to be created..."
sleep 2

echo "Applying infrastructure components (databases and brokers)..."
kubectl apply -f k8s/mysql
kubectl apply -f k8s/rabbitmq
kubectl apply -f k8s/postgres-telegram
kubectl apply -f k8s/postgres-notification

echo "Waiting 10s for DBs and RabbitMQ to become ready..."
sleep 10

echo " Applying microservices..."
kubectl apply -f k8s/user-service
kubectl apply -f k8s/time-tracking-service
kubectl apply -f k8s/notification-service
kubectl apply -f k8s/telegram-bot

echo " Deployment complete."
