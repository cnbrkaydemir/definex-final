# Tasks (Definex Java Spring Boot Bootcamp Final Project)
## Task management application for enterprises of all sizes.

Tasks is an advanced task management system that provides functionalities such as creating tasks, assigning tasks to users and managing teams, projects and more. Tasks resembles various project management tools one of them being Jıra by Atlassian. The tech stack used for the application consists of Java, Spring Boot, PostgreSQL, Redis and Docker.

<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" width="60"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" alt="Spring Boot" width="60"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/redis/redis-original.svg" alt="Redis" width="60"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" alt="Docker" width="60"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" alt="PostgreSQL" width="60"/>
</p>


## Table of Contents
1. [Installation](#installation)
2. [Features](#features)
3. [Deliverables](#deliverables)
4. [Tech Stack](#-tech-stack)
5. [Contribute and Next Steps](#next-steps)



## Installation


1. Before running the application make sure you have the following installed:
- Docker
- Docker-Compose
- Maven
- JDK 17 or later

2. Clone the repository with the command:
```ruby
git clone https://github.com/cnbrkaydemir/definex-final.git
```


3. Setup Environmental Variables:

Before running the application, you need to create a .env file and configure the database credentials.
* Create a .env file.

```ruby
touch .env
```
* Create a .env file and set the necessary variables:
```ruby
DB_USER=your_user_name
DB_PASSWORD=your_password
DB_ENTRY=database_name
```

4. Running Redis and PostgreSql with Docker Compose:

Once the .env file is ready, simply run:
```ruby
docker-compose up --build
```
To stop and remove the containers, run:

```ruby
docker-compose down
```

5. After the docker container is up and running run the application with the Maven command:
```ruby
mvn spring-boot:run
```

## Features
### Project and Task Management

- Created a comprehensive project management module with CRUD operations
- Implemented department association for projects
- Developed a task management system with detailed task information
- Built RESTful API endpoints for project and task operations

### Team Member Assignment

- Implemented user roles (Project Manager, Team Leader, Team Member)
- Created functionality for assigning team members to projects and tasks

### Progress Tracking

- Implemented the validation system for task progression required for an advanced task management system.

### File Storage for Attachments

- Built file upload and attachment system for tasks
- Implemented secure file storage on disk
- Created database entities for tracking uploaded files

### Comment Functionality
- Implemented business logic based on comments, enabling team members to assign comments to tasks.

### Security and Authentication

Implemented JWT-based authentication using oauth2 resource server.
Added role-based authorization
Created secure password handling through bcrypt password encoder

### Data Persistence and Soft Delete

Implemented JPA repositories with soft delete functionality
Added "deleted" flag to all entity models
Ensured queries filter out soft-deleted records by default
Implemented transaction management for data consistency

### Testing

Achieved over 90% code coverage with comprehensive unit tests
Tested both controller and service layers to validate business logic

### API Documentation

Created comprehensive postman collection for easy access to API endpoints. For more, import Tasks.postman_collection.json to Postman.

## Deliverables

### ER Diagram
![img_2.png](https://i.imgur.com/6KIcQeH.png)

### Relational Schema
![img_4.png](https://i.imgur.com/F72mKod.png)

### UML Class Diagram
![img_3.png](https://i.imgur.com/K5K690B.png)


## 🛠️ Tech Stack

- **Spring Boot**: Provides Inversion of Control (IoC) and Dependency Injection for building scalable applications.
- **Spring Data JPA**: Enables ORM (Object-Relational Mapping) and simplifies database interactions.
- **Spring Security**: Ensures secure API access using JWT-based authentication and password encoding with BCrypt.
- **PostgreSQL**: The relational database management system (RDBMS) used for storing database entities.
- **Redis**: Caches database queries to enhance performance and reduce load on PostgreSQL.
- **Docker**: Containerizes the application for easy deployment and scalability using Docker images and Docker Compose.  

## Next Steps

### **Performance Enhancements**
- Optimize database queries with indexing and caching.
- Implement pagination for API responses.

### **Feature Improvements**
- Add task dependencies (e.g., blocking tasks until prerequisites are completed).
- Add task and comment history.
