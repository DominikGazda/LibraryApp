# Library-app
#### Library application covered by tests
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#Features)
* [Rest documentation](#rest-documentation)

## General info
Library full stack application created by using Spring and Angular. It allows customer to loan books in library.

# Main page
## Features
* Search field to find book by name
* Add book to cart
* Click on book to see details
<img src = "https://github.com/DominikGazda/LibraryApp/blob/master/images/main.png" />

# Book details page
<img src = "https://github.com/DominikGazda/LibraryApp/blob/master/images/book-details.png" />

# Author page
## Features
* Show books assigned to  author
* Show author list
<img src = "https://github.com/DominikGazda/LibraryApp/blob/master/images/authors.png" />

# Cart page
## Features
* Show books in cart
* Increase or decrease books quantity in cart
<img src = "https://github.com/DominikGazda/LibraryApp/blob/master/images/cart-list.png" />

# Checkout page
<img src = "https://github.com/DominikGazda/LibraryApp/blob/master/images/checkout.png" />
	
## Technologies
Project is created with:
* Java 11
* Spring Boot 2.4.0
* Spring REST
* Spring/Bean Validation
* Spring Data
* Angular CLI version 11.2.0.
* TypeScript
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




