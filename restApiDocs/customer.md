# Customer Rest Documentation

## Table of contents
* [Get customers](#get-customers)
* [Save customer](#save-customer)
* [Get customer by id](#get-customer-by-id)
* [Update customer](#update-customer)
* [Delete customer](#delete-customer)
* [Get address for customer](#get-address-for-customer)
* [Get loans for customer](#get-loans-for-customer)

## Get customers
----
  Returns json data about all customers

* **URL**
  /api/customer
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
        "customerId": 1,
        "customerName": "Marcin",
        "customerSurname": "Polak",
        "address": {
            "addressId": 2,
            "city": "Mielec",
            "postalCode": "39-300"
        }
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if authors don't exist)```

## Save customer
----
  Save single customer in database

* **URL**
  /api/customer
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
{
    "customerId":null,
    "customerName":"Marcin",
    "customerSurname":"Polak",
    "address": 
    {
       "addressId":null,
       "city":"Mielec",
       "postalCode":"39-300"
    }
}
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
{
    "customerId":1,
    "customerName":"Marcin",
    "customerSurname":"Polak",
    "address": 
    {
       "addressId":1,
       "city":"Mielec",
       "postalCode":"39-300"
    }
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if customer has id)
    *  Message: ```Customer cannot have id```
  * **Code:** 400 Bad Request (if address has id)
    *  Message: ```Address cannot have id```
  * **Code:** 400 Bad Request  (if empty customerName field)
    * Message: ```Customer name cannot be empty```
  * **Code:** 400 Bad Request (if empty customerSurname field)
    * Message: ```Customer surname cannot be empty ```
## Get customer by id
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
    "customerId": 1,
    "customerName": "Marcin",
    "customerSurname": "Polak",
    "address": {
        "addressId": 2,
        "city": "Mielec",
        "postalCode": "39-300"
    }
}
 ```  
* **Error Response:**

  * **Code:** 404 Bad Request (if customer with provided id doesn't exist)
    * Message: ``` Cannot find customer with provided id ``` 
## Update customer
----
  Update single customer data

* **URL**
  /api/customer/{id}
* **Method:**
  `PUT`
*  **URL Params**
    `id=[Long}`

* **JSON Request Body:**
``` 
{
    "customerId": 1,
    "customerName": "Marcin",
    "customerSurname": "Polak",
    "address": {
        "addressId": 2,
        "city": "Mielec",
        "postalCode": "39-300"
    }
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
{
    "customerId": 1,
    "customerName": "Marcin",
    "customerSurname": "Polak",
    "address": {
        "addressId": 2,
        "city": "Mielec",
        "postalCode": "39-300"
    }
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if customer has id different from id in path variable)
    * Message: ```Customer must have id same as path variable ```
  * **Code:** 400 Bad Request (if address has id)
    *  Message: ```Address cannot have id```
  * **Code:** 400 Bad Request  (if empty customerName field)
    * Message: ```Customer name cannot be empty```
  * **Code:** 400 Bad Request (if empty customerSurname field)
    * Message: ```Customer surname cannot be empty ```
  
## Delete customer
----
  Delete single customer from database

* **URL**
  /api/customer/{id}
* **Method:**
  `DELETE`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
{
    "customerId": 1,
    "customerName": "Marcin",
    "customerSurname": "Polak",
    "address": {
        "addressId": 2,
        "city": "Mielec",
        "postalCode": "39-300"
    }
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if customer with provided id doesn't exist)
    * Message: ```Cannot find customer with provided id ```

## Get address for customer
----
  Returns address assigned to customer with provided id

* **URL**
  /api/customer/{id}/address
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
{
    "addressId": 3,
    "city": "Krakow",
    "postalCode": "32-100"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if customer with provided id doesn't exist)
    * Message: ```Cannot find customer with provided id ```
## Get loans for customer
----
  Returns loans assigned to customer with provided id

* **URL**
  /api/customer/{id}/loans
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
        "loanId": 1,
        "startLoanDate": "2021-01-10T15:50:51.990722",
        "returnLoanDate": null,
        "bookId": 1,
        "customerId": 2,
        "librarianId": 1,
        "active": true
    }
]
 ```  
* **Error Response:**

  * **Code:** 404 Not Found (if customer with provided id doesn't exist)
    *  Message: ```Cannot find customer with provided id```



