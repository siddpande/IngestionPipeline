# IngestionPipeline

The project is a simulation of an event ingestion pipeline.
Its developed using spring-boot.

![Screenshot](https://github.com/siddpande/IngestionPipeline/blob/master/Ingestion%20Pipeline.png)

**Steps to Run**
1) Clone te project to your system
2) Configure the Kafka Endpoints in application.properties
3) Go to the path ~/ingestion-pipeline/EventListener and run the command: ./mvnw spring-boot:run   

**Note:** *Currently only the EventIngestionService has been partially implemented for the purpose of the Demo.
All other services are just placeholders.*


**MicroServices in the System and their purpose**

1) **EventListener** This service act as an event synch and receives events from various sources/devices via Kafka. A sample event can look like:
{
  "sourcetype": "car",
  "identity": "f90c3dcd-27c3-4c2f-9fde-48dad0b5009d",
  "payload": "{\"speed\":\"10\",\"car\":{\"model\":\"tesla\",\"manufacturing\":\"2019\"}}",
  "timeStamp": 123456
}

The microservice dumps these events to its database so that they can later be used.
This service then forwards these events to an internal kafka topic from where the Analytics Service receives the event.

Database for EventListener microservice.
The microservice uses Cassandra as its database.
The main table looks like:

EventsTable
((sourceType, timestamp), identity), eventID, payload/payloadID()in case of object storage

The data is partitioned using source type and timestamp so that we can leverage the time stamp format for different sort of events.
In case the payloads are huge the system will leverage Swift Object storage and persist the payload ID in this table and the payload in object storage.
Another option when using object storage will be to have 2 table: 1 as shown above and we can have another one where the partition key has the source type and identity which ill helo in case identit based retrievals are done later.

This service will also have a smart rate limiter which will help in dropping traffic from fraudulent sources/ ID's.

2) **AnalyticsService**

This service receives all raw events from different sources.
It has event based parsers which parse different event types based on sources and extracts usefull information from huge data.
It then stores these parsed and structured events in any MariaDB database. We can use MariaDB here.
The service then publishes these structered events with important metadata to a kafka Topic to whch multiple services are listening.

3) **DataExtractionService**
This service is an interface from our system to external systems/ ML models.
It will retrieve data based on requests from external systems from our Raw events store or parsed events store. It can also read from the fraud detection and reporting services datastore.
We will expose API's here to allow customers to receive required data over synch as well as asynch route.
By design we would want this service to read from the Read Ony / Failover Database of services so that it doest put load on the active write DB of our system.
Eventuall consistency is acceptable to us for such data.

4) **FraudDetectionService**
This service reads parsed events from the anaytics and runs fraud detection algorthims on the data. It can also provide a feedback to the consumers in the Event Services to blacklist certain sources or user ID's data to prevent DDos like attacks.
It persists its Fraud logs and generates structured data which can be stored in a RDBS solution like MariaDB.

5)**ReportingService/ Logging Service**
This service listents to the parsed events being generated from the Analytics service.
It then generates reports / graphs as required by the system. It also persysts reports for archival purpose.
This service can also tap into the actual events beng receuved and log them if required.


**External Systems**
External Systems/ ML systems interact with us via the DataExtractionService service. THey receive data in a sync or async manner depenedng on the usecase.

**Other Points**
1) As the system scales and data increases we 


