### Command  to run mysql in docker
docker run --name mysql-timetracking \
-e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_DATABASE=timetracking_db \
-p 3306:3306 \
-d mysql:8.0
