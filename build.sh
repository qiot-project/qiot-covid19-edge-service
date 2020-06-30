./mvnw clean package -Pnative  -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
docker build -f src/main/docker/Dockerfile.native -t quay.io/qiot/qiot-service .
docker push quay.io/qiot/qiot-service