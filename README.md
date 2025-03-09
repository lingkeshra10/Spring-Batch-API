# Transaction Account Record System

The Transaction Account Record System allows users to upload a text file containing transaction records, which are then processed and stored in the database. Upon logging in, users receive a JWT token that grants them access to the system’s APIs for managing transaction records securely.

# Design Pattern

The project structure follows the Layered Architecture Pattern (also known as the N-Tier Architecture). This is a common design pattern in Spring Boot applications and follows the Separation of Concerns (SoC) principle.

 ## Breakdown of the Layers

1. Controller Layer (controller):
Handles incoming HTTP requests and responses.
Example: AuthController, TransAccController.

2. Service Layer (service & service.impl):
Contains business logic.
Example: TransAccRecordService, TransAccRecordServiceImpl.

3. Repository Layer (repository & repository.specification):
Handles database interactions using JPA/Hibernate.
Example: TransAccRecordRepo, TransAccRecordList.

4. Model Layer (model):
Contains data transfer objects (DTOs) used for request/response.
Example: ResponseModel, SearchTransAccRecordModel, UpdateTransAccRecordModel.

5. Entity Layer (entity):
Represents database entities (JPA entities).
Example: TransAccRecord.

6. Security Layer (security):
Manages security, authentication, and other configurations.
Example: JwtAuthFilter, SecureConfig.

7. Batch Processing Layer (batch):
Manages batch jobs.
Example: SpringBatchConfig, TransAccProcessor.

8. Other Design Patterns in Use
- Service Layer Pattern → Business logic is abstracted into services (TransAccRecordServiceImpl).
- Repository Pattern → Data access is handled separately in TransAccRecordRepo.
- Specification Pattern → Used in repository.specification.TransAccRecordList for dynamic queries.
- Singleton Pattern → JwtUtil and SecureConfig likely use singleton instances for security-related operations.

## Benefits of using the Layered Architecture Pattern

1. Separation of Concerns (SoC)
Each layer has a distinct responsibility:
- Controller Layer: Handles HTTP requests.
- Service Layer: Contains business logic.
- Repository Layer: Manages database interactions.
- Entity/Model Layer: Represents data structures.
This modularity makes the code cleaner, easier to maintain, and more understandable.

2. Scalability & Maintainability
- Changes in one layer don’t affect other layers directly.
- If I want to switch from JPA to MongoDB, then only modify the repository layer, not the whole application.
- If add business rules, we can modify only the service layer.
This makes application scalable and maintainable in the long run.

3. Testability
We can have unit test each layer separately.
- Example:
  - Mock repository layer when testing the service layer.
  - Mock service layer when testing the controller layer.
This improves test coverage and reduces dependency issues.

4. Reusability & Modularity
- Services can be reused across multiple controllers.
- Example:
  - If multiple controllers need to fetch transaction records, they can all call TransAccRecordService.
This reduces code duplication and increases reusability.

5. Security & Performance
- SecurityConfig ensures authentication & authorization in a centralized way.
- Caching & Optimizations can be added at specific layers.
  - Example: Add caching in the service layer to reduce database load.

6. Supports Design Patterns
- Repository Pattern ensures a clean way to interact with the database.
- Service Layer Pattern abstracts business logic.
- Specification Pattern allows dynamic queries.
This helps in writing flexible and robust code.

Why need to use this?
- If want clean, maintainable, and scalable code.
- If expect future modifications, such as switching databases, modifying authentication, or adding new features.
- If care about testability and performance.
- If work in a team, making it easier for others to understand and contribute.

Conclusion
This project follows a Layered Architecture with additional design patterns like Service, Repository, and Specification Pattern, ensuring modularity, maintainability, and scalability.

## List of API
Below is the list of API that need to access for the system.

| API Endpoint                         | Method | Description |
|--------------------------------------|--------|-------------|
| `/auth/login`                        | POST   | Authenticate user and generate JWT token. |
| `/transAcc/importData`               | POST   | Start a batch job to import transaction account records. |
| `/transAcc/updateData/{transAccRecId}` | PUT    | Update a transaction account record by ID. |
| `/transAcc/retrieveDataDetails/{transAccRecId}` | GET    | Retrieve transaction account record details by ID. |
| `/transAcc/retrieveDataList`         | GET    | Retrieve a list of all transaction account records. |
| `/transAcc/searchDataList`           | PUT    | Search for transaction account records based on criteria. |
