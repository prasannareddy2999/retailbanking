# Retail-Banking-System

Project Overview: 
-----------------
The Retail bank requires high availability for their operations to be available to their clients & bank employees without disruption. So it is required to provide a Micro-Service Architecture based software solution.

Retail Banking System helps to register a customer and create an account with the help of employee. Customer can perform withdrawal, deposit, transfer amount and can view the transaction details within the retail bank.

### Steps to install the project files

- Download the zip file of Retail Banking Management
- Extract the zip file.
- Import the spring boot project files into Spring Tool Suite/Eclipse IDE
- Run all the microservices as spring boot application
- Open the user interface files in Visual Studio Code/ Command Prompt
- Make sure the Node.js and Angular Client softwares have been installed in the system
- Open a new terminal navigate to the user interface files folder
- Give the command "npm install"
- After installing give the command "ng serve --open"
- The application will be opened in a browser window with url: http://localhost:4200/

Microservices:
---------------
   1. Authentication Microservice

   2. Customer Microservice

   3. Account Microservice

   4. Transaction Microservice

   5. Rules Microservice

Functionalities
-----------------
## 1. Authentication Microservice:

 Authentication Module is a Microservice that performs the following operations:
 - Login
 - Logout 
 

**Steps to follow:**
 
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8084/auth-ms/login (**Method:** POST)

**Step-3:** Provide the request body

**Step-4:** Click Send button,then a token will be generated.Copy the token.

**Step-5:** Open New tab in postman.

**Step-6:** Paste the Url http://localhost:8084/auth-ms/validateToken  (**Method:** GET)

**Step-7:** Go to Authorization Section, Select token type as Bearer , paste the token there.

**Step-8:** Click Send button, validation result will be generated.


## 2. Customer Microservice:

Customer Module is a Microservice that performs the following operations: 

- Create Customer 

- Get Customer Details 

a) Creating a customer profile
----------------------------- 
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8085/customer/createCustomer (**Method:** POST)

**Step-3:** Provide the request body

**Step-4:** Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-4:** Click Send button, customer creation status will be generated.


b) Dispaying customer’s profile:
---------------------------------
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8085/customer/getCustomerDetails/CUSTOMER101 (**Method:** GET)

**Step-3:** Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-4:** Click Send button, customer details will be fetched.

## 3. Account Microservice:

Account mnagement Module is a Microservice that performs following operations: 

- Get Customer Account(s) 

- Create Account 

- Get Account Statement 

- Withdraw 

- Deposit 

a) Depositing to a customer’s account:
---------------------------------------
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8086/account-ms/deposit (**Method:** POST)

**Step-3:** Provide the request body

**Step-4:** Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-5:** Click Send button, amount will be deposited.

b) Fetching Accounts related to a Customer as a summary information
------------------------------------------------------------------
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8086/account-ms/getAccount/1000000001 (**Method:** GET)

**Step-3:**  Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-4:** Click Send button, account details will be fetched.

c) Withdrawing from a customer’s account:
-------------------------------------------
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8086/account-ms/withdraw (**Method:** POST)

**Step-3:** Provide the request body

**Step-4:** Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-5:** Click Send button, amount will be withdrawn.

d) Transaction from a customer’s account:
---------------------------------------
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8086/account-ms/transaction (**Method:** POST)

**Step-3:** Provide the request body

**Step-4:** Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-5:** Click Send button, amount will be transferred.

e) Account statement of a customer account
-----------------------------------------
**Step-1:** Open Postman and follow the steps below:

**Step-2:** Go to Url section and paste http://localhost:8086/account-ms/getAccountStatement/1000000001 (**Method:** GET)

**Step-3:** Go to Authorization Section: Select token type as Bearer ,next paste the token there.

**Step-4:** Click Send button, account statement will be fetched.



## 4.Transaction Microservice:

Transactions Module is a Microservice that performs the following operations: 

- Deposit 

- Withdraw 

- Transfer 

- Get Transactions 


## 5. Rules Microservice:

The rules module is a microservice that will be responsible for evaluating rules while performing transactions like withdrawals, deposits. It will also return values based on the rules eg Minimum Account Balance, Service Charges for accounts that are not maintaining the minimum balance.

The following operations are performed in this module

- Evaluate Minimum Balance 

- Get Service Charges

User Interface
----------------------

The angular user interface loads the web application and takes care of user sessions. Relies on all other microservices for core functionality. 




## Retail Banking System - MFPE PROJECT 

 
|TeamMembers|Module Worked On|
|--|--|
|Bavani S | Account Service|
| Harish K|Rules Service |
| Mehul Kumar | Authentication Service |
|Vemula Chandana | Transaction Service  |
| Yash Gupta | Customer Service |

