# Currency Data Aggregator and REST API System

This system consists of several components designed to gather and serve currency 
data from [Fixer.io](https://fixer.io/). It updates currency data at predefined intervals
and provides two public REST APIs for external services (EXT_SERVICE_1 and EXT_SERVICE_2)
working with JSON and XML content types. Additionally, the system collects unified statistical
information about incoming requests and forwards this information via RabbitMQ.


## Prerequisites
- Java 21
- Maven
- Docker

## Components

1. **Currency Data Collection**:
    - Collects current currency data from [Fixer.io](https://fixer.io/) and stores it in a relational database.
    - Data is updated at predefined intervals, configurable via the application properties.

2. **Public REST APIs**:
    - Provides two public REST APIs for external services with JSON and XML content types.

### JSON API

Supports two endpoints that accept the following POST requests:

#### `/json_api/current`

```json
{
  "requestId": "b89577fe-8c37-4962-8af3-7cb89a245160",
  "timestamp": 1586335186721,
  "client": "1234",
  "currency": "EUR"
}
