---
paths:
  - "src/api/**/*.ts"
---

# API Service Rules

- Prefix exported API request functions with `companyApi`.
- Handle failed HTTP responses explicitly.
- Do not expose raw internal errors to callers.
- Add return types to exported API functions.

# java Specifications

- Use Java 17 for backend development.
- Use Spring Boot framework for building RESTful APIs.
- Use Maven for dependency management and build automation.
- Use JUnit 5 for unit testing and Mockito for mocking dependencies.