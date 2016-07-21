# Testing

## Unit Tests
* run in parallel
* no HTTP(s) connection or file touching
* sbt task `test` with tests located in "test" folder

## Play Tests
* require a started play application
* sbt task `pt:test` with tests located in `pt` folder

## Integration Tests
* sbt task `it:test` with tests located in `it` folder

## Acceptance Tests
* require a running application and external services
* see https://github.com/commercetools/commercetools-sunrise-scenarios