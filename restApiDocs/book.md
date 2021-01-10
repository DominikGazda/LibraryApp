# Book Rest Documentation

## Table of contents
* [Get books](#get-books)
* [Save book](#save-book)
* [Get book by id](#get-book-by-id)
* [Update book](#update-book)
* [Delete book](#delete-book)
* [Get authors for book](#get-authors-for-book)
* [Save author for book](#save-author-for-book)

## Get books
----
  Returns json data about all books

* **URL**
  /api/book
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
        "bookid": 1,
        "bookName": "W pustyni i w puszczy",
        "isbn": "9892038573123",
        "availableQuantity": 10,
        "publisher": "WSIP"
    },
    {
        "bookid": 2,
        "bookName": "Ferdydurke",
        "isbn": "9392857201921",
        "availableQuantity": 20,
        "publisher": "WSIP"
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if authors don't exist)```

## Save book
----
  Save single book in database

* **URL**
  /api/book
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
    {
        "bookid": null,
        "bookName": "W pustyni i w puszczy",
        "isbn": "9892038573123",
        "availableQuantity": 10,
        "publisher": "WSIP"
    }
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
    {
        "bookid": 1,
        "bookName": "W pustyni i w puszczy",
        "isbn": "9892038573123",
        "availableQuantity": 10,
        "publisher": "WSIP"
    }
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if book has id)
    *  Message: ```Book cannot have id```
  * **Code:** 400 Bad Request  (if empty bookName field)
    * Message: ```Book name cannot be empty```
  * **Code:** 400 Bad Request (if isbn hasn't 13 digits)
    * Message: ```Isbn number has to have 13 digits ```
  * **Code:** 400 Bad Request (if empty publisher field)
    * Message: ```Publisher field cannot be empty ```
 
## Get book by id
----
  Get single book with provided id

* **URL**
  /api/book/{id}
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
{
    "bookid": 1,
    "bookName": "W pustyni i w puszczy",
    "isbn": "9892038573123",
    "availableQuantity": 10,
    "publisher": "WSIP"
}
 ```  
* **Error Response:**

  * **Code:** 404 Bad Request (if book with provided id doesn't exist)
    * Message: ``` Cannot find book with provided id ``` 
## Update book
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
    "bookid": 1,
    "bookName": "W pustyni i w puszczy",
    "isbn": "9892038573123",
    "availableQuantity": 10,
    "publisher": "WSIP"
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
{
    "bookid": 1,
    "bookName": "W pustyni i w puszczy",
    "isbn": "9892038573123",
    "availableQuantity": 10,
    "publisher": "WSIP"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if book has id different from id in path variable)
    * Message: ```Book must have id same as path variable ```
  * **Code:** 400 Bad Request  (if empty bookName field)
    * Message: ```Book name cannot be empty```
  * **Code:** 400 Bad Request (if isbn hasn't 13 digits)
    * Message: ```Isbn number has to have 13 digits ```
  * **Code:** 400 Bad Request (if empty publisher field)
    * Message: ```Publisher field cannot be empty ```
  
## Delete book
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
    "bookid": 1,
    "bookName": "W pustyni i w puszczy",
    "isbn": "9892038573123",
    "availableQuantity": 10,
    "publisher": "WSIP"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if book with provided id doesn't exist)
    * Message: ```Cannot find book with provided id ```

## Get authors for book
----
  Returns authors assigned to book with provided id

* **URL**
  /api/book/{id}/authors
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
        "authorId": 2,
        "authorName": "Henryk",
        "authorSurname": "Sienkiewicz"
    }
]
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if book with provided id doesn't exist)
    * Message: ```Cannot find book with provided id ```
## Save author for book
----
  Assign author with provided id to book

* **URL**
  /api/book/{idBook}/authors/{idAuthor}
* **Method:**
  `POST`
*  **URL Params**
`idBook=[Long}`
`idAuthor=[Long}`

* **JSON Request Body:**
``` 
{
    "authorId": 1,
    "authorName": "Ernest",
    "authorSurname": "Hemingway"
}
 ```
* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
{
    "authorId": 1,
    "authorName": "Ernest",
    "authorSurname": "Hemingway"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if book has already assigned author with provided id)
    *  Message: ```Book already have assigned author with provided id```
  * **Code:** 400 Bad Request  (if author with provided id doesn't exist)
    * Message: ```Cannot find author with provided id"```


