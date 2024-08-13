# Funds Transfer Service

This service is mainly responsible for managing transfers between 'Accounts'.

## Quick guide

- To add a new table, alter an existing one or any other db operation please add a 'migration' file in `src/main/resources/db.migration`
- The current DB configuration is using MYSQL db and the configuration properties are found
  in `src/main/resources/application.properties` you can use H2 in memory db by commenting the
  `MySql Configuration` part and commenting `H2 Configuration` part.
    - Note: the current H2 configuration persists the DB on file.
      see: `spring.datasource.url=jdbc:h2:file:./data/fund_transfer_service`

- [freecurrencyapi](https://freecurrencyapi.com/docs) is used to retrieve current conversion rates. The api is free and
  is updated daily. For the API to work some configuration properties must be provided.
    - `clients.freeCurrencyApi.base-url` Representing the base URL for the api
    - `clients.freeCurrencyApi.api-key` Representing the api key that is used for authentication by the API

> Caching is configured to cache the API response and the cache is cleared at midnight. this is done to save network
> overhead and to make sure the the free api quota is not met.
>  - caching
     config: `src/main/java/com/example/fundTransferService/external/client/FreeCurrencyApiClient.java` `@Cacheable(value = "getLatestRates")`
>  - caching
     clearing: `src/main/java/com/example/fundTransferService/external/client/FreeCurrencyApiClient.java` `clearGetLatestRatesCache()`

- A variation of Strategy design pattern is used in the service for:
    - A) Defining business rules in `src/main/java/com/example/fundTransferService/business/strategy/rule/FundsTransferRule.java`
      - Implementations of that interface should provide new business rules that should be met
      - This was it is much easier to maintain funds transfer rule, easier to add new rules and the code of funds transfer is decoupled from checking constraint
    - B) Defining currency exchange strategies: `src/main/java/com/example/fundTransferService/business/strategy/exchange/ExchangeStrategy.java`
      - Implementations of that interface should provide new strategies for transferring funds;
            for example, the system now doesn't have a strategy for transferring funds outside EU, a business case that could have different implementation like adding extra fees
      - Again, this way it is much easier to maintain and extend currency conversion strategies

- Multiple business exceptions were defined in `src/main/java/com/example/fundTransferService/exception` to have more control over exception handling in our system. Thrown exceptions are handled
        in `src/main/java/com/example/fundTransferService/exception/ExceptionHandler.java` which uses Spring's `@ControllerAdvice`.
        This way when an exception is caught it is processed and a descriptive error response is returned by the API

- There a use of "Factory": `src/main/java/com/example/fundTransferService/factory` and "Mappers" (mapstruct): `src/main/java/com/example/fundTransferService/business/mapper`:
        -- The philosophy here is to use factories when there is some logic in creating new instances like generating IBAN or TransactionId, and for simple mapping between objects mappers are used

- Swagger is used to document rest APIs. check: http://localhost:8080/swagger-ui/index.html#

## Testing

    Unit and Integrations tests were added for different components in the system. but note that, some functionalities were left out of testing on purpose to save time. the reason is that those
    components, like adding account holders or creating accounts are kind of out of scope of the task and only serve a complementary purpose.
