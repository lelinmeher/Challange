# Money Transfer Challenge

## Overview
- Simple REST api application to support concurrent money transfer requests
- Application server listens on localhost:8080
- Uses In-memory synchronized datastore with ReentrantLock
- Ability to Create, get account and transfer money between accounts
- Validations
    - Duplicate Account
    - Account Doesn't Exist
    - Insufficient Funds for transfer
    - Illegal transfer amount (< 0)
- Libraries
    - Junit
    - lombok

## Suggestion on pending tasks:

- We should have an actuator endpoint enabled to check application health and get the information of running applications.
- We should have logging mechanism with correlation  ID for all the requests. 
- We can  create BDD test cases to test end to end application with acceptance criteria
- We can  have acceptance tests and integration tests .
