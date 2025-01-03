# GameRent System

## Overview

GameRent is a system designed to manage the rental of video games. It includes modules for user management, game catalog, rental system, favorites, and reviews & feedback.

## Modules

1. **User Management**
   - Handles user registration, authentication, and profile management.
2. **Game Catalog**
   - Manages the list of available games for rent, including details and availability.
3. **Rental System**
   - Manages the rental process, including rental requests, returns, and rental history.
4. **Favorites Module**
   - Allows users to mark games as favorites for easy access.
5. **Reviews & Feedback**
   - Enables users to leave reviews and feedback for games.

## Dependencies

The project dependencies are managed using Maven and are listed in the [pom.xml](pom.xml) file. Key dependencies include:

- Spring Boot Starter Data JPA
- MySQL Connector/J
- Spring Boot Starter JDBC
- Spring Boot Starter Security
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Web
- Thymeleaf Extras Spring Security 6
- Spring Boot Starter Test
- Spring Security Test

## Getting Started

To get started with the GameRent system, follow these steps:

1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   ```
2. **Navigate to the project directory:**
   ```
   cd gamerent
   ```
3. **Build the project:**

   ```
   ./mvnw clean install
   ```

4. **Run the application:**
   ```
   ./mvnw spring-boot:run
   ```

**Access the application:**
Open your browser and navigate to http://localhost:8080.

**Configuration**
The application properties are configured in the application.properties file. Update the database configuration as needed.

**License**
This project is licensed under the MIT License. See the LICENSE file for details.

**Contributing**
Contributions are welcome! Please open an issue or submit a pull request.

**Team**
Harith Bennet
Janot
Sahil
Maliha
Amin
