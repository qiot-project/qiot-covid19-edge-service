./mvnw clean package -Pnative  -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
docker rmi quay.io/qiot/qiot-service --force
docker build -f src/main/docker/Dockerfile.native -t quay.io/qiot/qiot-service .
docker push quay.io/qiot/qiot-service
#docker run -it --rm -p 8080:8080 --net host quay.io/qiot/qiot-service
