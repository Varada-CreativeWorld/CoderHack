# CoderHack User Management API

This project is a RESTful API for managing users in the CoderHack platform. It allows clients to perform CRUD (Create, Read, Update, Delete) operations on user data.

## Features

- **Get All Users:** Retrieve a list of all registered users.
- **Get User by ID:** Retrieve a specific user by their user ID.
- **Register User:** Register a new user with a unique user ID and username.
- **Update User Score:** Update the score for an existing user, ensuring the score is within the valid range of 0 to 100.
- **Delete User:** Delete a user by their user ID.

## PostMan Collection Link

https://www.postman.com/warped-satellite-11630/workspace/criotakehomes/collection/20005493-376046ce-b156-457a-8c87-a3ab32d272b7?action=share&creator=20005493

## Endpoints

### 1. Get All Users

- **URL:** `/users`
- **Method:** `GET`
- **Response:**
  - `200 OK`: Returns a list of all users.

### 2. Get User by ID

- **URL:** `/users/{userId}`
- **Method:** `GET`
- **Path Variable:** 
  - `userId` (String): The ID of the user to retrieve.
- **Response:**
  - `200 OK`: Returns the user with the specified user ID.
  - `404 NOT FOUND`: If the user with the specified ID does not exist.

### 3. Register User

- **URL:** `/users`
- **Method:** `POST`
- **Request Body:** 
  - `UserDTO` (JSON): The user data to register.
    - `userId` (String): The unique ID for the user. Must not be blank.
    - `username` (String): The username for the user. Must not be blank.
- **Response:**
  - `201 CREATED`: Returns the newly created user.
  - `400 BAD REQUEST`: If the user ID or username is missing or if a user with the same ID already exists.

### 4. Update User Score

- **URL:** `/users/{userId}`
- **Method:** `PUT`
- **Path Variable:** 
  - `userId` (String): The ID of the user whose score is to be updated.
- **Request Parameter:**
  - `score` (int): The new score to set for the user. Must be between 0 and 100.
- **Response:**
  - `200 OK`: Returns the updated user.
  - `400 BAD REQUEST`: If the score is not within the valid range.
  - `404 NOT FOUND`: If the user with the specified ID does not exist.

### 5. Delete User

- **URL:** `/users/{userId}`
- **Method:** `DELETE`
- **Path Variable:** 
  - `userId` (String): The ID of the user to delete.
- **Response:**
  - `204 NO CONTENT`: User was successfully deleted.
  - `404 NOT FOUND`: If the user with the specified ID does not exist.

## Exceptions

- **UserNotFoundException:** Thrown when a requested user ID does not exist in the system.
- **InvalidScoreException:** Thrown when an invalid score is provided (e.g., score is less than 0 or greater than 100).
- **MethodArgumentNotValidException:** Thrown when validation on input data fails (e.g., missing user ID or username).

## Running Tests

To run the tests, use the following Gradle command:

```bash
./gradlew test
