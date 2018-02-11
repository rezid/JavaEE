#!/usr/bin/env bash
docker stop glassfish-instance
docker rm glassfish-instance
docker image rm --force glassfish-war-image

docker build -t glassfish-war-image ..
docker run -d -p 8080:8080 -p 4848:4848 --name glassfish-instance  glassfish-war-image
