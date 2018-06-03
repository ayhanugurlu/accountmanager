# Account Manager

![alt text](https://travis-ci.org/silayugurlu/accountmanager.svg?branch=master "Travis Status")

[![codecov.io](https://codecov.io/github/silayugurlu/accountmanager/coverage.svg?branch=master)](https://codecov.io/github/cainus/codecov.io?branch=master)

This project consists of APIs to be used in banking for customer operations like creating account, making transaction, getting customer, account and transaction informations.

## Getting Started

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.capgemini.assessment.AccountManagerApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

When the application is running, you can see endpoints from link below:

[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
