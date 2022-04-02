# Start with a base image containing Java runtime
FROM openjdk:17

# Marking port 8001 available to the world outside this container
EXPOSE 8001

ADD target/student-courses-1.0-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]