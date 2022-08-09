# Start with a base image containing Java runtime
FROM openjdk:17

ENV user user

RUN mkdir /var/java-docker-demo/

COPY /src/ /var/java-docker-demo/

WORKDIR /var/java-docker-demo/

# The run instruction executes when we build the image. That means the command passed to run
# executes on top of the current image in a new layer.
RUN javac dev/bumbler/JavaDockerDemo.java

# CMD ["ls", "-R"]
ENTRYPOINT ["java", "dev/bumbler/JavaDockerDemo"]
