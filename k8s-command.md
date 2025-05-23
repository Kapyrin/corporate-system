

# Команды для работы с Kubernetes, использованные в процессе отладки

# 1. Показать все поды во всех namespaces
kubectl get pods --all-namespaces

# 2. Показать поды в конкретном namespace
kubectl get pods -n time-tracking-ns
kubectl get pods -n user-service-ns

# 3. Получить логи пода (последние 20 строк)
kubectl logs time-tracking-service-5754c8ff69-hx6l7 -n time-tracking-ns --tail=20
kubectl logs user-service-579f8dc459-92pwb -n user-service-ns --tail=20

# 4. Показать описание пода (события, статус)
kubectl describe pod user-service-579f8dc459-92pwb -n user-service-ns
kubectl describe pod mysql-78b767d9b5-cvbp5 -n mysql-ns

# 5. Показать все PVC во всех namespaces
kubectl get pvc --all-namespaces

# 6. Показать PVC в конкретном namespace
kubectl get pvc -n user-service-ns
kubectl get pvc -n mysql-ns

# 7. Показать описание PVC (события, статус)
kubectl describe pvc user-service-pvc -n user-service-ns

# 8. Показать описание сервиса
kubectl describe svc user-service -n user-service-ns
kubectl describe svc time-tracking-service -n time-tracking-ns

# 9. Проверить DNS для сервиса из пода
kubectl exec -it time-tracking-service-5754c8ff69-hx6l7 -n time-tracking-ns -- nslookup user-service.user-service-ns.svc.cluster.local

# 10. Перезапустить поды через удаление
kubectl delete pod -l app=time-tracking-service -n time-tracking-ns
kubectl delete pod -l app=user-service -n user-service-ns

kubectl rollout restart deployment telegram-bot -n telegram-bot-ns


# 11. Применить изменения в ConfigMap
kubectl apply -f k8s/time-tracking-service/configmap.yaml

# 12. Удалить и пересоздать сервис
kubectl delete -f k8s/user-service/service.yaml
kubectl apply -f k8s/user-service/service.yaml

# 13. Показать StorageClass и PersistentVolume
kubectl get storageclass
kubectl get pv

# 14. Удалить старые PersistentVolume
kubectl delete pv pvc-88e61fd2-c6f8-4ae9-852d-fe070a3ecf6e

# 15. Выполнить запрос внутри пода для проверки сервиса
kubectl exec -it telegram-bot-7f74b99c5-zsbg4 -n telegram-bot-ns -- wget --server-response -O - http://user-service.user-service-ns.svc.cluster.local:8080/api/users/1/exists

kubectl config set-cluster docker-desktop --server=https://127.0.0.1:6443
