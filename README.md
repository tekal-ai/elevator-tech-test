# Memorable Technical Test: build an elevator system

## Part A: 

For Part A I've decided to create a Web Controller for the Elevator System. The Web Controller is a Spring Boot + Java 
application that exposes a REST API to interact with the underlying Elevator System.

### Usage

To begin using the Elevator System, clone this repository to your local machine:

```shell
git clone https://github.com/tomassirio/elevator-tech-test.git
cd ./elevator-tech-test
```

Build the project using the Maven wrapper:

```shell
./mvnw clean verify
```

Run the project using the Maven wrapper:

```shell
./mvnw spring-boot:run
```

Once the project is running, you can interact with the Elevator System using the following REST API:

#### Elevator API

You can interact with it via CURL or Postman. The following endpoints are available:

- **GET**   /v1/elevator/services -> Returns the list of available Elevator Services
- **POST**  /v1/elevator/activate -> Activates an Elevator Service
- **POST** /v1/people/add         -> Adds a Person to the Elevator System

##### Examples

```shell
curl http://localhost:8080/v1/elevator/services
```

```shell
curl -X POST "http://localhost:8080/v1/elevator/activate?serviceName=FCFSElevatorService"
```

```shell
curl -X POST http://localhost:8080/v1/people/add -H "Content-Type: application/json" -d '{"calledFromFloor": 1, "destinationFloor": 5}'
```

#### Open-API

Additionally, you can interact with the API through the browser following this link http://localhost:8080/swagger-ui/index.html

### Elevator System

The Elevator System Creates at Startup 3 Elevators, A Queue for Each ElevatorService, and a HashMap representing the 
building and the floors.

At Runtime, the Elevator System can receive pickup requests from the Web Controller. A pickup request creates a 
Person + an ElevatorCall to be processed by the Active ElevatorService.

### Technical Choices

### Asynchronous Processing
The Elevator System is designed to be asynchronous. The Elevator System receives an ElevatorCall request synchronously throught
the Web Controller, but then the Elevator System processes the request asynchronously. This is done to have elevators
work in parallel and to avoid blocking the Web Controller while returning a Response

### ElevatorService
The ElevatorService is designed as an Interface which can then be implemented by different algorithmic choices.
Each time an ElevatorService is implemented, the ElevatorServiceManager will add the implementation to the current available
ElevatorServices.
At Runtime one would be able to switch between different ElevatorServices by sending a POST message through the ElevatorController.

#### Implemented Services:
 - FCFSElevatorService: First Come First Served Elevator Service
 - ScanElevatorService: Scan Elevator Service
 - CScanElevatorService: Circular Scan Elevator Service
 - TuViejaElevatorService: **Obligatory** - Tu Vieja Elevator Service

### ElevatorManager and ElevatorContextController
In order to grant scalability and encapsulation, the ElevatorManager is designed as a Singleton which carries the 
instance of ElevatorService active at the moment. This ElevatorService can be changed during Runtime via the EventContextController

---

## Part B:

The Simulator is implemented over a REST controller with a Single Endpoint:

- POST /v1/simulation/run -> Runs the Simulation

The Simulator is designed to be asynchronous. The Simulator receives a SimulationRequest synchronously through
the Web Controller, but then the Simulator processes the request asynchronously

### Usage

Run an instance of the application and run the following CURL command:

```shell
curl -X POST http://localhost:8080/v1/simulation/run -H "Content-Type: application/json" -d '{"simulationSeed": 123, "durationInSeconds": 10}'
```

Alternatively, you can interact with the API through the browser following this link http://localhost:8080/swagger-ui/index.html

A Response will be returned with the simulation results.
