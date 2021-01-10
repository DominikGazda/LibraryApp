# Address Rest Documentation

## Table of contents
* [Get All Addresses](#get-all-addresses)
* [Save address](#save-client)
* [Get address by id](#get-address-by-id)
* [Update address](#update-address)
* [Delete address](#delete-address)

## Get All Addresses
----
  Returns json data about all addresses

* **URL**
  api/address/
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
        "addressId": 1,
        "city": "Mielec",
        "postalCode": "33-100"
    }
]
 ```  
* **Error Response:**
```No error response (returns empty list if addresses don't exist)```

## Save address
----
  Save single address in database

* **URL**
  api/address/
* **Method:**
  `POST`
*  **URL Params**
none

* **JSON Request Body:**
``` 
    {
        "addressId": null,
        "city": "Mielec",
        "postalCode": "33-100"
    }
 ```
* **Success Response:**

  * **Code:** 201 
    **Content:** 
``` 
    {
        "addressId": 1L
        "city": "Mielec",
        "postalCode": "33-100"
    }
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if address has id)
    *  Message: ```Address cannot have id ```
  * **Code:** 400 Bad Request  (if empty city field)
    * Message: ```City field cannot be empty```
  * **Code:** 400 Bad Request (if postalCode field doesn't match to pattern)
    * Message: ```Postal Code must match the expression 00-000```
 
## Get address by id
----
  Show single address with provided id

* **URL**
  api/address/{id}
* **Method:**
  `GET`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200
    **Content:** 
``` 
 {
    "addressId": 1,
    "city": "Mielec",
    "postalCode": "33-100"
}
 ```  
* **Error Response:**

  * **Code:** 404 Bad Request (if address with provided id doesn't exist)
    * Message: ``` Address with provided id not found ``` 
## Update address
----
  Update single address data

* **URL**
  api/address/{id}
* **Method:**
  `PUT`
*  **URL Params**
    `id=[Long}`

* **JSON Request Body:**
``` 
{
    "addressId": 1,
    "city": "Krakow",
    "postalCode": "32-100"
}
 ```
* **Success Response:**

  * **Code:** 201 Created
    **Content:** 
``` 
{
    "addressId": 1,
    "city": "Krakow",
    "postalCode": "32-100"
}
 ```  
* **Error Response:**

  * **Code:** 400 Bad Request (if address has id different from id in path variable)
    * Message: ```Address must have id same as path variable ```
  
## Delete address
----
  Delete single address from database

* **URL**
  api/address/{id}
* **Method:**
  `DELETE`
*  **URL Params**
    `id=[Long}`

* **Success Response:**

  * **Code:** 200 OK
    **Content:** 
``` 
{
    "addressId": 1,
    "city": "Krakow",
    "postalCode": "32-100"
}
 ```  
* **Error Response:**

  * **Code:** 404 Not found (if address with provided id doesn't exist)
    * Message: ```Address with provided id not found ```

