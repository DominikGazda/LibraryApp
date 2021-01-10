# Publisher Rest Documentation

## Table of contents
* [Get publishers](#get-publishers)
* [Save publisher](#save-publisher)
* [Get publisher by id](#get-publisher-by-id)
* [Update publisher](#update-publisher)
* [Delete publisher](#delete-publisher)
* [Get books for publisher](#get-books-for-publisher)
## Get publishers
----
  Returns json data about all publishers

* **URL**
  /api/publisher
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
        "publisherId": 1,
        "publisherName": "WSIP"
    },
    {
        "publisherId": 2,
        "publisherName": "Nowa Era"
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if publishers don't exist)```

## Save publisher
----
  Save single publisher in database

* **URL**
  /api/publisher
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
{
    "publisherId": null,
    "publisherName": "WSIP"
}
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
{
    "publisherId": 3,
    "publisherName": "WSIP"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if publisher has id)
    *  Message: ```Publisher cannot have id```
  * **Code:** 400 Bad Request  (if empty publisherName field)
    * Message: ```Publisher name cannot be empty```

    
## Get publisher by id
----
  Get single publisher with provided id

* **URL**
  /api/publisher/{id}
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
{
    "publisherId": 1,
    "publisherName": "WSIP"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if publisher with provided id doesn't exist)
    * Message: ``` Cannot find publisher with provided id ``` 
## Update publisher
----
  Update single publisher data

* **URL**
  /api/publisher/{id}
* **Method:**
  `PUT`
*  **URL Params**
    `id=[Long}`

* **JSON Request Body:**
``` 
{
        "publisherId":1,
        "publisherName": "Nowa Era"
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
{
        "publisherId":1,
        "publisherName": "Nowa Era"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if publisher has id different from id in path variable)
    * Message: ```Publisher must have id same as path variable ```
  * **Code:** 400 Bad Request  (if empty publisherName field)
    * Message: ```Publisher name cannot be empty```
  
## Delete publisher
----
  Delete single publisher from database

* **URL**
  /api/publisher/{id}
* **Method:**
  `DELETE`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
{
        "publisherId":1,
        "publisherName": "Nowa Era"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if publisher with provided id doesn't exist)
    * Message: ``` Cannot find publisher with provided id ``` 
## Get books for publisher
----
 Get book assigned to publisher with provided id

* **URL**
  /api/publisher/{id}/books
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
[
    {
        "bookid": 1,
        "bookName": "W pustyni i w puszczy",
        "isbn": "9892038573123",
        "availableQuantity": 9,
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

  * **Code:** 404 Not found (if publisher with provided id doesn't exist)
    * Message: ``` Cannot find publisher with provided id ``` 


