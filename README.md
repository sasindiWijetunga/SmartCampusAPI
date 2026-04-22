# SmartCampusAPI
RESTful Smart Campus API implemented with Java and JAX-RS.
The solution deals with management of rooms, sensors, and sensor values.
It provides means to create and read these resources via HTTP endpoints.
The application emulates smart campus and works with sensors that measure
temperature, CO2 level etc.

## Technologies Used
- Java
- JAX-RS (Jersey)
- Maven
- JSON(Jackson)

## How to Build and Run
1. Open the project in NetBeans or IntelliJ
2. Build the project using Maven
3. Run the project on the server
4. Open the browser or Postman and use the API

Base URL:
http://localhost:8080/SmartCampusAPI/api/v1/

## API Endpoints:

### Rooms:
GET /api/v1/rooms
GET /api/v1/rooms/{roomId}
POST /api/v1/rooms
DELETE /api/v1/rooms/{roomId}

### Sensors:
GET /api/v1/sensors
GET /api/v1/sensors/{sensorId}
GET /api/v1/sensors?type=TYPE
POST /api/v1/sensors

### Sensor Readings:
GET /api/v1/sensors/{sensorId}/readings
POST /api/v1/sensors/{sensorId}/readings

## Business Rules
- A room cannot be deleted if it has sensors assigned
- A sensor must be linked to an existing room
- Sensor readings cannot be added if the sensor is in MAINTENANCE status
- Adding a reading will update the current value of the sensor

## Error Handling
- RoomNotEmptyException → 409 Conflict
- LinkedResourceNotFoundException → 422 Unprocessable Entity
- SensorUnavailableException → 403 Forbidden
- GlobalExceptionMapper → 500 Internal Server Error

## Logging 
The logger will keep a record of all the incoming requests and outgoing responses 
including HTTP method, request URL, and response status code.

## Data Storage
The API usesan in-memory data storage structure is implemented using HashMaps and ArrayLists. 
No database is required according to the coursework specifications.

## Sample Curl Commands

### 1. Get all rooms 
curl -X GET http://localhost:8080/api/v1/rooms
### 2. Create a new room 
curl -X POST http://localhost:8080/api/v1/rooms
-H "Content-Type: application/json" 
-d "{"id":"ROOM-001","name":"Test Room","capacity":20}"
### 3. Get all sensors filtered by type
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
### 4. Create a new sensor
curl -X POST http://localhost:8080/api/v1/sensors
-H "Content-Type: application/json"
-d "{"id":"SENS-001","type":"CO2","status":"ACTIVE","currentValue":350.0,"roomId":"LIB-301"}"
### 5. Add a sensor reading
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings 
-H "Content-Type: application/json" 
-d "{"value":25.5}"

## Report - Answers to Questions 

### Part 1 
**Question 1 - In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions**

**Answer -** The default JAX-RS behaviour is to create a new instance of a resource class with each request. This is known as per-request lifecycle. This implies that every request receives a new object. Due to this we cannot store data on resource class instance variables because it will be lost after every request. This is the reason why we adopt a fixed DataStore class using HashMaps to store data. Shared fields are static and exist throughout the life of the application and do not lose data between requests.

**Question 2 - Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach
benefit client developers compared to static documentation?**

**Answer -** HATEOAS implies that API responses contain links to other resources and actions that can be performed. This is regarded as an indicator of well-developed RESTful design since clients need not hard-code URLs instead they can find them dynamically based on the responses. This is an advantage to client developers as they do not have to use some kind of static documentation to understand what they can do. The API is self-documenting and simpler to navigate instead of the API telling them what to do and what is available to them.


### Part 2
**Question 3 - When returning a list of rooms, what are the implications of returning only
IDs versus returning the full room objects? Consider network bandwidth and client side
processing**

**Answer -** Returning only IDs consume less network bandwidth since the response payload is significantly smaller. The client has to then make further requests to retrieve information about each room which adds additional API calls and latency. Full room objects consume more bandwidth but take lesser network round trips. A system with a large number of rooms returning fully complete objects is usually preferable to a campus system because it does not need as much client side processing and it performs well due to the lack of many follow-up requests.

**Question 4 - Is the DELETE operation idempotent in your implementation? Provide a detailed
justification by describing what happens if a client mistakenly sends the exact same DELETE
request for a room multiple times.**

**Answer -** Yes DELETE is idempotent as implemented. When a client repeats the same DELETE request a number of times the final outcome will be the same - the room is non existent. The first request successfully deletes the room and returns 200 OK. Further requests to the same room will get 404 Not Found since the room is no longer there. The response code does not match the server state after the initial deletion but the state is the same hence the operation is idempotent.


### Part 3
**Question 5 - We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on
the POST method. Explain the technical consequences if a client attempts to send data in
a different format, such as text/plain or application/xml. How does JAX-RS handle this
mismatch?**

**Answer -** 
