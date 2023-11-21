# Deployment

## Deploy MySQL

```text
kubectl apply -f mysql-secret.yaml,mysql-storage.yaml,mysql-deployment.yaml
```

## Connect to MySQL

List pods

```text
kubectl get pods
```

Connect to MySQL's stdin

```text
kubectl exec --stdin --tty <mysql pod name> -- /bin/bash
```

Login to MySQL in console

```text
mysql -p
then enter password
```

Forward mySQL port to be able to connect to it from outside (for debug purposes).
Port to forward should be taken from the mySQL Kubernetes deployment yaml.

```text
kubectl port-forward service/mysql 3306
```

Take forwarded port from the command output:

```text
Forwarding from 127.0.0.1:3306 -> 3306
Forwarding from [::1]:3306 -> 3306
```

Install mysql shell on your Windows machine using winget from PowerShell or from [dev.mysql.com](https://dev.mysql.com/downloads/shell/)

Connect to MySQL port-forwarded instance

```text
\connect mysql://root@localhost:3306
```

In Shell, switch to SQL mode and use SQL syntax like 'SHOW DATABASES'

```text
 MySQL  localhost:3306  JS > \sql

 MySQL  localhost:3306  SQL > SHOW DATABASES;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| books              |
| mysql              |
| performance_schema |
| testDB             |
+--------------------+
```

Create books and records databases

```text
 MySQL  localhost:3306  SQL > CREATE DATABASE registry;
 MySQL  localhost:3306  SQL > CREATE DATABASE library;
```

## Build docker image and Deploy BookRegistry Service

```text
docker build -t bookregistryservice .
```
Run book registry service
```text
kubectl apply -f book-registry-deployment.yaml
```
Make sure that pod is running
```text
kubectl get pods
```
Forward service port to be able to connect to the service
```text
kubectl port-forward service/book-registry 8080
```
=======
## Build docker image and Deploy Library Service

```text
docker build -t libraryservice .
```
Run library service
```text
kubectl apply -f library-deployment.yaml
```
Make sure that pod is running
```text
kubectl get pods
```
Forward service port to be able to connect to the service
```text
kubectl port-forward service/library 8081
```

## Build docker image and Deploy Authentication Service

```text
docker build -t authenticationservice .
```
Run authentication service
```text
kubectl apply -f authentication-deployment.yaml
```
Make sure that pod is running
```text
kubectl get pods
```
Forward service port to be able to connect to the service
```text
kubectl port-forward service/authentication 8082
```
