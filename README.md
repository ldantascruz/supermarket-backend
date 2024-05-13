# Supermarket API

## Overview
This project is a backend API developed in Java using Spring Boot and Spring Security. It supports a "Market List" mobile application where users can manage their shopping lists, tracking product names, quantities, and prices to calculate total expenses during shopping. This backend is crucial for user authentication and product management.

## Features
- User authentication with JWT.
- CRUD operations for products.
- Deployed using Docker on render.com.

## Technology Stack
- Java
- Spring Boot
- Spring Security
- Docker

## Deployment
The API is containerized with Docker and deployed to render.com.

### Docker Configuration
```dockerfile
FROM ubuntu:latest AS build
RUN apt-get update && apt-get install openjdk-17-jdk maven -y
COPY . .
RUN mvn clean install

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```


## Configuration
### Set the following environment variables in your deployment environment:
- DATABASE_URL
- DATABASE_USERNAME
- DATABASE_PASSWORD

### API Endpoints
- /products (GET, POST, PUT, DELETE)
- /users (GET, POST, PUT, DELETE)


### Future Scope
- Detailed product management.
- Generating financial reports and inventory alerts based on user spending.

## License
### This project is released under the MIT License.