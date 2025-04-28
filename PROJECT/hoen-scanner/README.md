# Hoen Scanner Microservice

This project is a Dropwizard-based Java microservice for Skyscanner.

It accepts a city name in a POST request and returns available hotels and rental car services for that city.

## Setup

1. Clone the project.
2. Open in IntelliJ and load Maven project.
3. Run the project.

## Usage

Send a POST request to:

```
http://localhost:8080/search
```

Example Request Body:

```json
{
  "city": "petalborough"
}
```

## Output

A list of hotels and rental car services available in the specified city.

---

Good luck and happy scanning!
