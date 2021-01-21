docker rmi quay.io/qiot/qiot-service:1-x86_64 --force
docker build -f src/main/docker/Dockerfile.native.multiarch -t quay.io/qiot/qiot-service:1-x86_64 .
#docker push quay.io/qiot/qiot-service:1-x86_64
#docker run -it --rm -p 8080:8080 --net host quay.io/qiot/qiot-service
