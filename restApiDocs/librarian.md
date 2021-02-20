# Librarian Rest Documentation

## Table of contents
* [Get librarians](#get-librarians)
* [Save librarian](#save-librarian)
* [Get librarian by id](#get-librarian-by-id)
* [Update librarian](#update-librarian)
* [Delete librarian](#delete-librarian)

## Get librarians
----
  Returns json data about all librarians

* **URL**
  /api/librarian
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
        "librarianId": 1,
        "librarianName": "Marek",
        "librarianSurname": "Kowal"
    },
    {
        "librarianId": 2,
        "librarianName": "Jurek",
        "librarianSurname": "Kozak"
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if authors don't exist)```

## Save librarian
----
  Save single librarian in database

* **URL**
  /api/librarian
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
  {
        "librarianId": null,
        "librarianName": "Marek",
        "librarianSurname": "Kowal"
}
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
  {
        "librarianId": 1,
        "librarianName": "Marek",
        "librarianSurname": "Kowal"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if librarian has id)
    *  Message: ```Librarian cannot have id```
  * **Code:** 400 Bad Request  (if empty librarianName field)
    * Message: ```Librarian name cannot be empty```
  * **Code:** 400 Bad Request (if empty librarianSurname field)
    * Message: ```Librarian surname cannot be empty ```
    
## Get librarian by id
----
  Get single customer with provided id

* **URL**
  /api/customer/{id}
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
{
    "librarianId": 1,
    "librarianName": "Marek",
    "librarianSurname": "Kowal"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if librarian with provided id doesn't exist)
    * Message: ``` Cannot find librarian with provided id ``` 
## Update librarian
----
  Update single librarian data

* **URL**
  /api/librarian/{id}
* **Method:**
  `PUT`
*  **URL Params**
    `id=[Long}`

* **JSON Request Body:**
``` 
{
    "librarianId": 1,
    "librarianName": "Marek",
    "librarianSurname": "Kowal"
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
{
    "librarianId": 1,
    "librarianName": "Marek",
    "librarianSurname": "Kowal"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if librarian has id different from id in path variable)
    * Message: ```Librarian must have id same as path variable ```
  * **Code:** 400 Bad Request  (if empty librarianName field)
    * Message: ```Librarian name cannot be empty```
  * **Code:** 400 Bad Request (if empty librarianSurname field)
    * Message: ```Librarian surname cannot be empty ```
  
## Delete librarian
----
  Delete single librarianr from database

* **URL**
  /api/librarian/{id}
* **Method:**
  `DELETE`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
{
    "librarianId": 1,
    "librarianName": "Marek",
    "librarianSurname": "Kowal"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if librarian with provided id doesn't exist)
    * Message: ``` Cannot find librarian with provided id ``` 



