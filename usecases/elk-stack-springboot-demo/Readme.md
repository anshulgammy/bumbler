# Read Me First

This demo application makes use of ELK stack for the Centralized logging.

* Elasticsearch, Logstash, and Kibana, are being used in this demo, which are running in their separate containers.

* For Kibana, and Logstash, I have used their dedicated configuration files, which are being
supplied to the docker container via docker-compose.

* ELK stack can be started using the docker-compose file. But before you run it, make sure that jar file
for the Spring Application is created first, else you will get error when you do docker-compose up.

* Spring Boot application used in this demo is using log4j configuration to send the logs to TCP port 9999.
At this port, Logstash will listen to the logs, and send that to Elasticsearch via 'logstash-docker' index.

### Once all the containers are running:
* Access student-courses api at URL: http://localhost:8001/v1/api/courses
* Access Kibana at: http://localhost:5601/
* Access Elasticsearch indices at: http://localhost:9200/_cat/indices
* Logstash is running at port 9999, and listening to logs sent over TCP by student-courses APIs.


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

