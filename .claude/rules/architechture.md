# Architecture Guidelines Rules

Always separate: 
# Backend Structure
- Controller (API layer) 
   - Controller should only handle request/response and delegate business logic to the service layer.
- Service (business logic) 
   - Service should Have Serice layer, service implementation to extract business logic from the controller and repository. 
- Repository (DB access) 
   - Repository should handle data access and persistence.
- DTOs (API contracts only)
   - DTOs should define the structure of data exchanged between the API and clients.
- Common (shared utilities)
   - Common should contain shared utilities, helper functions, and reusable
- Properties (configuration)
   - Properties should contain configuration files and settings for different environments (e.g., development, production).
- Logs
   - Logs should contain log files and logging configuration.

# Frontend Stucture
- Components (UI layer) 
   - Components should be responsible for rendering the UI and handling user interactions.
   - Services (business logic)
   - Services should handle business logic and data fetching, separate from the UI components.
   - Common (shared utilities)
   - Common should contain shared utilities, helper functions, and reusable components that can be used across the frontend.
   - Routes (navigation)
   - Routes should define the application's navigation structure and handle routing logic.
   - Environment (configuration)
   - Environment should contain configuration files and settings for different environments (e.g., development, production)

# DB Structure
- Entities (data models)
   - Entities should define the structure of the data stored in the database. 
- Repositories (data access)
   - Repositories should handle data access and persistence for the entities.
- Migrations (schema changes)
   - Migrations should manage database schema changes and versioning.
-In-memory (temporary data)
   - In-memory should handle temporary data storage and caching, if applicable.


