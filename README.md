
# A Simple Url Shortener

## Overview
This project is intended to use a simple url shortener to 
demonstrate the web application architecture based on Java, 
Spring Boot, MySQL, Microservices, Docker Container, Node JS, 
and kafka. Users can use it as a template for your microservices 
based web applications.

## Setup Instructions
Make sure Docker Desktop is installed. Please refer to [Docker Desktop](https://www.docker.com/products/docker-desktop/) 
for more information.

To run this application, type "docker-compose up" under the project root directory.

## API Documentation
Link to the Swagger API documentation: 
[localhost:8080](http://localhost:8080/swagger-ui.html)
[localhost:8081](http://localhost:8081/swagger-ui.html)

## Dependencies
- Java 17
- Spring Boot 3.3.0
- Springdoc OpenAPI 2.5.0
- Node 16
- Spring for Apache Kafka 3.2.0
- Resillience4J
- jwt for authentication
- Lombok
- MapStruct

## Build and Deployment
To build and run this application on your local machine, type "docker-compose up"
under the project root directory.

Go to http://localhost:3000 to test the Url Shortener API and Url Shortener Statistics API.

Kubernetes deployment files are located under /kubernetes. For local development, use the ***-node-port 
service file to expose MySQL and Kafka services to your app.

## Docker Containers
The application runs with the following Docker containers:
- MySQl
- ZooKeeper (required by Kafka)
- Kafka (for event logging)
- the Url Shortener API
- the Url Shortener Statistics API, 
- the Testing Webpage (React JS with hooks. Vite for packaging)

## Coming soon
- Data engineering using Apache Flink

## Contact
qinlihai@gmail.com
