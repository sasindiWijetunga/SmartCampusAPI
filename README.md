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

**Answer -** When a client sends data, but in text/plain or application/xml rather than application/json JAX-RS will automatically respond with a 415 Unsupported Media Type. This is due to the fact that the @Consumes(Mediatype.APPLICATION_JSON) annotation informs JAX-RS to accept only the requests whose Content-Type is application/json. When the Content-Type header is not JAX-RS rejects the request even before it has been meets our resource approach. This helps to ensure that the API does not get unexpected data in unforeseen formats that might lead to parsing errors.

**Question 6 - You implemented this filtering using @QueryParam. Contrast this with an alternative
design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why
is the query parameter approach generally considered superior for filtering and searching
collections?**

**Answer -** Filtering with query parameters is better than using path parameters since query parameters are optional and endsure the base URL is clean.For example:
Base URL - /api/v1/sensors → returns all sensors
With filter: /api/v1/sensors?type=CO2 → returns only CO2 sensors
In case the type appeared in the URL path (e.g. /api/v1/sensors/type/CO2), it would be a distinct resource instead of a filter which is semantically incorrect in REST. The query parameters are also more adaptable since several filters can be easily combined:
/api/v1/sensors?type=CO2&status=ACTIVE
This maintains the URL clean and easy to maintain as opposed to the path parameters that would be making the URL structure vicious to manage with the addition of more filters.

### Part 4 
**Question 7 - Discuss the architectural benefits of the Sub-Resource Locator pattern. How
does delegating logic to separate classes help manage complexity in large APIs compared
to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller
class?**

**Answer -** Sub-Resource Locator pattern enables complex APIs to be divided into small focused classes rather than having one huge controller class managing all the endpoint. A single responsibility exists in each of the classes and the code is simpler to understand and test. Dynamically SensorReadingResource is used as a reading logic but SensorResource is used as a sensor logic. Such isolation of issues brings a more organised and scalable codebase. Nested resources can be added by creating a new class and not by making changes to existing ones which can lead to the introduction of bugs.

### Part 5
**Question 8 - Why is HTTP 422 often considered more semantically accurate than a standard
404 when the issue is a missing reference inside a valid JSON payload?**

**Answer -** The 404 HTTP response indicates that the resource requested at the specified URL was not located. The meaning of HTTP 422 is that the request was grammatical, but unable to be processed because of semantic errors. In case a client POSTs a valid JSON sensor with a non-existent roomId the URL is valid and the JSON structure is valid. The issue is a broken reference in the payload. Therefore 422 Unprocessable Entity is more semantically accurate because it communicates that the request body contains a logical error rather than the endpoint not existing.

**Question 9 - From a cybersecurity standpoint, explain the risks associated with exposing
internal Java stack traces to external API consumers. What specific information could an
attacker gather from such a trace?**

**Answer -** The issue of exposing Java stack traces is a critical security threat because it exposes internal information, including class names, method names, line numbers and framework versions. This can be used by attackers to determine vulnerabilities and develop targeted attacks. To avoid this we intercept all unexpected errors with our GlobalExceptionMapper and send a generic 500 message.

**Question 10 - Why is it advantageous to use JAX-RS filters for cross-cutting concerns like
logging, rather than manually inserting Logger.info() statements inside every single resource
method?**

**Answer -** Logging with JAX-RS filters is preferable to inserting Logger.info() lines into each resource method. Using filters, the logging logic is specified at one point and is automatically used on each incoming request and outgoing response at the entire API. Manual logging involves the insertion of a code in each and every method resulting in the creation of duplicated code and complicates the maintenance of the API. When adding a new endpoint the developer should not forget to add logging manually, which is susceptible to errors. Filters embrace the DRY principle and enable logging behaviour to be modified at a single point without any business logic being touched.
