# Currency Data Aggregator and REST API System

This system consists of several components designed to gather and serve currency 
data from [Fixer.io](https://fixer.io/). 
It updates currency data at predefined intervals
and provides two public REST APIs for external services (EXT_SERVICE_1 and EXT_SERVICE_2)
working with JSON and XML content types.
Additionally, the system collects unified statistical
information about incoming requests and forwards this information via RabbitMQ.


## Prerequisites
- Java 21
- Maven
- Docker
- RabbitMQ

## Components

1. **Currency Data Collection**:
    - Collects current currency data from [Fixer.io](https://fixer.io/) and stores it in a relational database.
    - Data is updated at predefined intervals (with current config - 1h ), configurable via the application properties.

2. **Public REST APIs**:
    - Provides two public REST APIs for external services with JSON and XML content types.
    - On Request saves in Cache the latest rate available for choosen currency from the Database.
    - On Request saves in Cache the latest ratesavailable for choosen currency within 24h period.
    - It uses that cached value for future requests.
    - Caches are getting deleted when new data its synchronized from Fixer.
      
### JSON API

- Supports two endpoints that accept the following POST requests:

#### `/json_api/current`

```json
{
  "requestId": "b89577fe-8c37-4962-8af3-7cb89a245160",
  "timestamp": 1586335186721,
  "client": "1234",
  "currency": "EUR"
}
```
- Request Handling:

*Checks for duplicate requests by requestId. If a duplicate is found, the system returns an error.*

*The client field identifies the end client.*

*On successful execution, the response contains the latest data for the specified currency.*

#### `/json_api/history`
```json
{
  "requestId": "b89577fe-8c37-4962-8af3-7cb89a24q909",
  "timestamp": 1586335186721,
  "client": "1234",
  "currency": "EUR",
  "period": 24
}
```
- Request Handling:

*Checks for duplicate requests by requestId. If a duplicate is found, the system returns an error.*

*The client field identifies the end client.*

*The response contains a list of accumulated data for the specified currency over the given period (in hours).*

### XML API
- Provides a single endpoint for POST requests, which can be in the following formats:

#### `/xml_api/command` 
- For Current Data:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<command id="1234">
  <get consumer="13617162">
    <currency>EUR</currency>
  </get>
</command>
```

- For Historical Data:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<command id="1234-8785">
  <history consumer="13617162" currency="EUR" period="24" />
</command>
```

- Request Handling:

*The id attribute in the command tag uniquely identifies the request.*

*The consumer attribute is interpreted as the end client's ID.*

*Duplicate request checks are implemented based on the id.*

### Unified Statistical Information Collection

- Collects and stores statistical information (service name/id, request id, time (UTC), end client id)
in a relational database for incoming requests from EXT_SERVICE_1 and EXT_SERVICE_2.

### Forwarding Request Information via RabbitMQ

- Forwards unified information about incoming requests through a web socket (RabbitMQ).
- The exchange name is predefined in the configuration.

## Getting Started

#### Clone the Repository
```shell
git clone https://github.com/randered/gateway.git
```

#### Build the Project
```shell
mvn clean install
```

#### Install Docker
[Docker installation guide](https://docs.docker.com/engine/install/)

#### Postgres Database with Docker
```shell
docker run --name gateway_db -e POSTGRES_DB=gateway -e POSTGRES_USER=user -e POSTGRES_PASSWORD=12345 -d -p 5432:5432 postgres
```

#### RabbitMQ with Docker 
```shell
docker run -d --name gateway-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

#### Environment variables
```
API_KEY=(Your API key for fixer.io) ;EXCHANGE=gateway (or whatever exchange you have)
```

### The Project will initially generate some data automatically with the DataLoader, so if you want just to test it without calling the external api just comment out the @Scheduled annotation in RatesDataLoaderService.

### Verify Messages in RabbitMQ
- Access the RabbitMQ management interface:

*Open your browser and go to http://localhost:15672.*
*Log in with the default username and password (guest).*
*Navigate to the "Queues" tab.*
*Find test_queue and verify that the messages are present.*
