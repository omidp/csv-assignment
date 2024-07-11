## Csv data loader

Overview

csv data loader is a Java-based application designed to allow uploading and getting a csv file.

## Features

- Feature 1: upload the data
- Feature 2: Fetch all data
- Feature 3: Fetch by code

Prerequisites
To run this project, you will need the following:

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6.3 or higher

## How to build

``` 
mvn clean install
```

## How to run

``` 
java -jar target/data-0.0.1-SNAPSHOT.jar
```

## How to test

```
curl --location 'http://localhost:8080/csv/fetch'
```

Or 

You can import the postman collection (csv-assignment.postman_collection).

## Things to discuss or improve

- Adding Swagger
- Adding DAO/Repository
- Handling huge files
- Handling more date format
- Handling unique codes efficiently
- Logging and prod configuration
- Testing improvement

