docker run --rm --privileged multiarch/qemu-user-static:register --reset
docker rmi quay.io/qiotcovid19/edge-service:2.0.0-aarch64 --force
docker build -f src/main/docker/Dockerfile.native.multiarch -t quay.io/qiotcovid19/edge-service:2.0.0-aarch64 .
docker push quay.io/qiotcovid19/edge-service:2.0.0-aarch64
#docker run -it --rm -p 8080:8080 --net host quay.io/qiot/qiot-service
