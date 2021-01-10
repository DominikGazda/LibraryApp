# Author Rest Documentation

## Table of contents
* [Get authors](#get-authors)
* [Save author](#save-author)
* [Get author by id](#get-author-by-id)
* [Update author](#update-author)
* [Delete author](#delete-author)
* [Get books for author](#get-books-for-author)

## Show clients
----
  Returns json data about all authors

* **URL**
  /api/author
* **Method:**
  `GET`
*  **URL Params**
none

* **Success Response:**

  * **Code:** 200 
  * **Content:** 
``` 
 [
    {
        "authorId": 1,
        "authorName": "Ernest",
        "authorSurname": "Hemingway"
    },
    {
        "authorId": 2,
        "authorName": "Henryk",
        "authorSurname": "Sienkiewicz"
    },
    {
        "authorId": 3,
        "authorName": "Adam",
        "authorSurname": "Małysz"
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if authors don't exist)```

## Save author
----
  Save single author in database

* **URL**
  /api/clients
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
{
    "authorId": null,
    "authorName": "Marcin",
    "authorSurname": "Autor"
}
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
 {
    "authorId": 5,
    "authorName": "Marcin",
    "authorSurname": "Autor"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if author has id)
    *  Message: ```Author cannot have id```
  * **Code:** 400 Bad Request  (if empty authorName field)
    * Message: ```Author name cannot be empty```
  * **Code:** 400 Bad Request (if empty authorSurname field)
    * Message: ```Author surname cannot be empty```
 
## Get author by id
----
  Get single author with provided id

* **URL**
  /api/author/{id}
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
 {
    "authorId": 5,
    "authorName": "Marcin",
    "authorSurname": "Autor"
}
 ```  
* **Error Response:**

  * **Code:** 404 Bad Request (if author with provided id doesn't exist)
    * Message: ``` Cannot find author with provided id ``` 
## Update author
----
  Update single author data

* **URL**
  /api/author/{id}
* **Method:**
  `PUT`
*  **URL Params**
    `id=[Long}`

* **JSON Request Body:**
``` 
 {
    "authorId": 5,
    "authorName": "Marcin",
    "authorSurname": "Autor"
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
 {
    "authorId": 5,
    "authorName": "Marcin",
    "authorSurname": "Autor"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if author has id different from id in path variable)
    * Message: ```Author must have id same as path variable ```
  * **Code:** 400 Bad Request  (if empty authorName field)
    * Message: ```Author name cannot be empty```
  * **Code:** 400 Bad Request (if empty authorSurname field)
    * Message: ```Author surname cannot be empty```
  
## Delete author
----
  Delete single author from database

* **URL**
  /api/author/{id}
* **Method:**
  `DELETE`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
 {
    "authorId": 5,
    "authorName": "Roman",
    "authorSurname": "Damian"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if author with provided id doesn't exist)
    * Message: ```Cannot find author with provided id ```

## Get books for author
----
  Returns books assigned to author with provided id

* **URL**
  api/author/{id}/books
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
[
    {
        "bookid": 1,
        "bookName": "W pustyni i w puszczy",
        "isbn": "9892038573123",
        "availableQuantity": 10,
        "publisher": "WSIP"
    }
]
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if author with provided id doesn't exist)
    * Message: ``` Cannot find author with provided id ``` 


