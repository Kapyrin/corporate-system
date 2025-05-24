#!/bin/bash

echo "🛑 Cleaning up workloads, keeping PVC and namespaces..."

NAMESPACES=(
  mysql-ns
  rabbitmq-ns
  postgres-ns
  postgres-notification-ns
  user-service-ns
  time-tracking-ns
  notification-ns
  telegram-bot-ns
)

for ns in "${NAMESPACES[@]}"; do
  echo " Cleaning namespace: $ns"
  kubectl delete deployment --all -n "$ns" --ignore-not-found
  kubectl delete statefulset --all -n "$ns" --ignore-not-found
  kubectl delete service --all -n "$ns" --ignore-not-found
  kubectl delete configmap --all -n "$ns" --ignore-not-found
  kubectl delete secret --all -n "$ns" --ignore-not-found
  kubectl delete ingress --all -n "$ns" --ignore-not-found
done

echo "Workloads removed, data preserved (PVC and namespaces not deleted)."



##!/bin/bash
#
#echo " Deleting all resources and namespaces..."
#
#kubectl delete -f k8s/telegram-bot --ignore-not-found
#kubectl delete -f k8s/time-tracking-service --ignore-not-found
#kubectl delete -f k8s/user-service --ignore-not-found
#kubectl delete -f k8s/notification-service --ignore-not-found
#kubectl delete -f k8s/postgres-telegram --ignore-not-found
#kubectl delete -f k8s/postgres-notification --ignore-not-found
#kubectl delete -f k8s/rabbitmq --ignore-not-found
#kubectl delete -f k8s/mysql --ignore-not-found
#
#
#kubectl delete namespace postgres-ns --ignore-not-found
#kubectl delete namespace time-tracking-ns --ignore-not-found
#kubectl delete namespace user-service-ns --ignore-not-found
#kubectl delete namespace notification-ns --ignore-not-found
#kubectl delete namespace postgres-telegram-ns --ignore-not-found
#kubectl delete namespace postgres-notification-ns --ignore-not-found
#kubectl delete namespace rabbitmq-ns --ignore-not-found
#kubectl delete namespace mysql-ns --ignore-not-found
#
#echo "--- All resources deleted."
