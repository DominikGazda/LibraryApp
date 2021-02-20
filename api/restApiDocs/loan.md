# Loan Rest Documentation

## Table of contents
* [Get loans](#get-loans)
* [Save loan](#save-loan)
* [Get loan by id](#get-loan-by-id)
* [Update loan](#update-loan)
* [Delete loan](#delete-loan)

## Get loans
----
  Returns json data about all loans

* **URL**
  /api/loan
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
        "loanId": 1,
        "startLoanDate": "2021-01-10T15:50:51.990722",
        "returnLoanDate": null,
        "bookId": 1,
        "customerId": 2,
        "librarianId": 1,
        "active": true
    },
    {
        "loanId": 2,
        "startLoanDate": "2021-01-10T16:56:52.801978",
        "returnLoanDate": null,
        "bookId": 1,
        "customerId": 2,
        "librarianId": 1,
        "active": true
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if loans don't exist)```

## Save loan
----
  Save single loan in database

* **URL**
  /api/loan
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
{
    "loanId":null,
    "startLoanDate":null,
    "returnLoanDate":null,
    "active":true,
    "bookId":2,
    "customerId":2,
    "librarianId":2
}
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
{
    "loanId":1,
    "startLoanDate":"2021-01-10T15:50:51.990722",
    "returnLoanDate":null,
    "active":true,
    "bookId":2,
    "customerId":2,
    "librarianId":2
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if loan has id)
    *  Message: ```Loan cannot have id```
  * **Code:** 400 Bad Request  (if startLoanDate field is not empty)
    * Message: ```Loan start date must be empty```
  * **Code:** 400 Bad Request  (if bookId field is null)
    * Message: ```Loan must have assigned book``` 
  * **Code:** 400 Bad Request  (if customerId field is null)
    * Message: ```Loan must have assigned customer```
   * **Code:** 400 Bad Request  (if librarianId field is null)
     * Message: ```Loan must have assigned librarian```  
## Get loan by id
----
  Get single loan with provided id

* **URL**
  /api/loan/{id}
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
{
    "loanId": 1,
    "startLoanDate": "2021-01-10T15:50:51.990722",
    "returnLoanDate": null,
    "bookId": 1,
    "customerId": 2,
    "librarianId": 1,
    "active": true
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if loan with provided id doesn't exist)
    * Message: ``` Cannot find loan with provided id ``` 
## Update loan
----
  Update single loan data

* **URL**
  /api/loan/{id}
* **Method:**
  `PUT`
*  **URL Params**
    `id=[Long}`

* **JSON Request Body:**
``` 
{
    "loanId": 1,
    "startLoanDate": "2021-01-10T15:50:51.990722",
    "returnLoanDate": null,
    "bookId": 1,
    "customerId": 2,
    "librarianId": 1,
    "active": true
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
{
    "loanId": 1,
    "startLoanDate": "2021-01-10T15:50:51.990722",
    "returnLoanDate": null,
    "bookId": 1,
    "customerId": 2,
    "librarianId": 1,
    "active": true
}
 ```  
* **Error Response:**
    * **Code:** 400 Bad Request (if loan has id different from id in path variable)
        * Message: ```Loan must have id same as path variable ```
     * **Code:** 400 Bad Request  (if startLoanDate field is not empty)
        * Message: ```Loan start date must be empty```
    * **Code:** 400 Bad Request  (if bookId field is null)
        * Message: ```Loan must have assigned book``` 
    * **Code:** 400 Bad Request  (if customerId field is null)
        * Message: ```Loan must have assigned customer```
    * **Code:** 400 Bad Request  (if librarianId field is null)
         * Message: ```Loan must have assigned librarian```  
  
## Delete Loan
----
  Delete single loan from database

* **URL**
  /api/loan/{id}
* **Method:**
  `DELETE`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
{
    "loanId": 5,
    "startLoanDate": "2021-01-10T17:07:12.8499713",
    "returnLoanDate": null,
    "bookId": 1,
    "customerId": 2,
    "librarianId": 1,
    "active": true
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if loan with provided id doesn't exist)
    * Message: ``` Cannot find loan with provided id ``` 


