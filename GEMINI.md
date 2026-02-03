# Project Overview

This project is a Spring Boot starter for ZK, a Java framework for building web applications. It simplifies the integration of ZK with Spring Boot by providing auto-configuration and a starter POM. The project is divided into three main modules:

*   **`zkspringboot-starter`**: The main starter that provides the necessary dependencies to use ZK in a Spring Boot application.
*   **`zkspringboot-autoconfig`**: Contains the auto-configuration classes that configure ZK components in the Spring environment.
*   **`zkspringboot-demos`**: A collection of demo applications that showcase how to use the ZK Spring Boot starter.

## Building and Running

The project is built with Maven. To build the project, run the following command from the root directory:

```bash
mvn clean install
```

To run the demo applications, navigate to the respective demo directory (e.g., `zkspringboot-demos/zkspringboot-minimal-jar`) and run the following command:

```bash
mvn spring-boot:run
```

## Development Conventions

The project follows standard Java and Spring Boot conventions. ZUL files, which are used to define the user interface, are typically placed in the `src/main/resources/web/zul` directory. Spring controllers are used to handle user interactions and business logic.

The project also provides a set of configuration properties that can be used to customize the behavior of the ZK integration. These properties are defined in the `ZkProperties.java` class in the `zkspringboot-autoconfig` module.
