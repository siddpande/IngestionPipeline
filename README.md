# IngestionPipeline

The project is a simulation of an event ingestion pipeline.
Its developed using spring-boot.

![Screenshot](https://github.com/siddpande/IngestionPipeline/blob/master/Ingestion%20Pipeline.png)

Steps to Run:
1) Clone te project to your system
2) Configure the Kafka Endpoints in application.properties
3) Go to the path ~/ingestion-pipeline/EventListener and run the command: ./mvnw spring-boot:run   

Note: Currently only the EventIngestionService has been partially implemented for the purpose of the Demo.
All other services are just placeholders.
