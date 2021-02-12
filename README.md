# Library-app
#### Library application covered by tests
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#Features)
* [Rest documentation](#rest-documentation)
* [To Do](#to-do)
## General info
Library RESTful API created by using Spring
	
## Technologies
Project is created with:
* Java 11
* Spring Boot 2.4.0
* Spring REST
* Spring/Bean Validation
* Spring Data
* JPA/Hibernate
* JUnit 5.6.0
* Mockito 3.4.4
* H2 Database
	
## Setup
To run this project, install it locally using npm:

1. Download or Clone project:
```
https://github.com/DominikGazda/LibraryApp.git
```
2. Import project as maven
```
Import -> Import as Maven project
```
3. Run LibraryApplication.class
4. Go to url below (application is using embedded server)
```
http://localhost:8080/
```
5. To check database go to url below
```
http://localhost:8080/h2-console
```

## Features

* borrow books in libary
* return books to library

## Rest documentation
*  [Address entity](/restApiDocs/address.md)
*   [Author entity](/restApiDocs/author.md)
*  [Book entity](/restApiDocs/book.md)
*  [Customer entity](/restApiDocs/customer.md)
*  [Librarian entity](/restApiDocs/librarian.md)
*  [Loan entity](/restApiDocs/loan.md)
*  [Publisher entity](/restApiDocs/publisher.md)


