chmod +x mvn*
mvn -N io.takari:maven:wrapper
./mvnw clean package
# -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
docker rmi quay.io/qiotcovid19/edge-service:2.0.0-alpha-aarch64-jvm
sudo podman build -f src/main/docker/Dockerfile.jvm -t quay.io/qiotcovid19/edge-service:2.0.0-alpha-aarch64-jvm .
docker push quay.io/qiotcovid19/edge-service:2.0.0-alpha-aarch64-jvm
