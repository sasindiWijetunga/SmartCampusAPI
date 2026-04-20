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
