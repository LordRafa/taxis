# taxis

Autor: Rafael Waldo Delgado Doblas

This project is a distributed application that allows to manage a basic taxi company.

It is divided in three microservices:
* The Hub service which is the interface exposed to the customers. It allows to list taxis and create the trips.
* The Car service which is the application that will onboard in the taxis. It allows to update the location of the taxis and reply/complete to the trips.
* The Central service which the application with the access to the database and coordinated the other two applications.

Mostly of the communications are synchronous but the taxis will be posted in a rabbitMQ in a fanout exchange to notify the taxis that a new trip has been created, to allow multicast the same message to all the taxis.

To run this you need to have installed docker and docker-compose.

To run the project you need to execute the following command:

```bash
sh run.sh
```

Three containers will be created, one for each service.

To make things easier frontends can be used to interact with the services:
* The Hub frontend: http://localhost:42025
* The Car 1 frontend: http://localhost:42026
* The Car 2 frontend: http://localhost:42027
* The Central frontend: http://localhost:42024/central/stats

The Hub frontend allows to list the taxis and create trips.
The Car frontend allows to update the location of the taxis and reply/complete to the trips.
The Central frontend allows to see the statistics of the trips.

Each taxi will require to have an instance of the Car service running.
