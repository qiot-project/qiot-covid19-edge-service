chmod +x mvn*
mvn -N io.takari:maven:wrapper
mvn clean package
# -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
sudo podman rmi quay.io/qiotcovid19/edge-service:2.0.0-aarch64-jvm
sudo podman build -f src/main/docker/Dockerfile.jvm.uberjar -t quay.io/qiotcovid19/edge-service:2.0.0-aarch64-jvm .
sudo podman push quay.io/qiotcovid19/edge-service:2.0.0-aarch64-jvm
