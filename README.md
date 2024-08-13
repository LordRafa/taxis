# Taxi Management System

**Author:** Rafael Waldo Delgado Doblas

## Overview

This project is a distributed application designed to manage a basic taxi company. It consists of three microservices, each serving a unique function within the system.

### Microservices

1. **Hub Service**
   - **Purpose:** Acts as the main interface for customers.
   - **Functions:**
     - List available taxis.
     - Create and manage trips.

2. **Car Service**
   - **Purpose:** Operates on individual taxis.
   - **Functions:**
     - Update taxi location in real-time.
     - Respond to and complete trip requests.

3. **Central Service**
   - **Purpose:** Manages the system's database and coordinates the other two services.
   - **Functions:**
     - Access and maintain trip data.
     - Oversee communication between Hub and Car services.

### Communication

- **Synchronous Communication:** Most interactions between services are synchronous.
- **Asynchronous Notifications:** Taxis are notified of new trip requests via a RabbitMQ fanout exchange, allowing simultaneous multicast notifications to all taxis.

## Prerequisites

To run this project, ensure you have the following installed:

- **Docker**
- **Docker Compose**

## Running the Project

To launch the application, execute the following command in your terminal:

```bash
sh run.sh
```

This will spin up four containers, each hosting one of the services.

## Frontend Interfaces

To interact with the services, you can use the following frontends:

- **Hub Frontend:** [http://localhost:42025](http://localhost:42025)
  - **Functions:** List taxis, create trips.
- **Car 1 Frontend:** [http://localhost:42026](http://localhost:42026)
  - **Functions:** Update taxi location, manage trips.
- **Car 2 Frontend:** [http://localhost:42027](http://localhost:42027)
  - **Functions:** Update taxi location, manage trips.
- **Central Frontend:** [http://localhost:42024/central/stats](http://localhost:42024/central/stats)
  - **Functions:** View trip statistics.

## Notes

- Each taxi requires an instance of the Car Service to be running.
