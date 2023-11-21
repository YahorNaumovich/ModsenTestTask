# ModsenTestTask

This project consists of three microservices meant to be deployed using Kubernetes:

## BookRegistryService
Stores information about the books and provides methods to manipulate this information (Create, update, read, delete).

## LibraryService
Stores Ids of books that are stored in BookRegistryService and provides methods for book reservation and return.

## AuthenticationService
Is used to generate a token for authentication in the services mentioned above.


Project uses Spring boot, Hibernate, JPA, mySQL, Swagger UI and authentication via bearer token.


Instructions for deployment here: https://github.com/YahorNaumovich/ModsenTestTask/blob/main/deployment/README.md