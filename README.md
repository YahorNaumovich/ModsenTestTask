# ModsenTestTask

This project consists of three microservices meant to be deployed using Kubernetes. The microservices are listed below.

Project uses Spring boot, Hibernate, JPA, mySQL, Swagger UI and authentication via bearer token.

You can deploy this project using the [instruction](https://github.com/YahorNaumovich/ModsenTestTask/blob/main/deployment/README.md).

The microservices were tested in Rancher Desktop running on Windows 10 and WSL2.

## AuthenticationService

Emulates user authentication.

This service is used to generate a valid bearer token for authentication in the services mentioned below.

## BookRegistryService

Emulates a Registry of books.

Stores information about books and provides methods to manipulate this information (create, update, read, delete).

Synchronizes its content with the Library Service.

Full list of methods can be seen in the service's [Controller](https://github.com/YahorNaumovich/ModsenTestTask/blob/main/sources/BookRegistryService/src/main/java/com/example/bookregistryservice/controller/BookController.java).

## LibraryService

Emulates a Library of books.

When a book appears in the Registry, a book becomes available in the Library. A user can order this book for reading and return it back.

There are additional functionalities such as returning list of 'booked' and 'unbooked' books.

Full list of methods can be seen in the service's [Controller](https://github.com/YahorNaumovich/ModsenTestTask/blob/main/sources/LibraryService/src/main/java/com/example/libraryservice/controller/LibraryController.java).
