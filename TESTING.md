## Testing

### Unit Tests
* run in parallel
* no HTTP(s) connection or file touching
* sbt task `test` and tests in "test" folder

### Play Tests

* require a started play application
* sbt task `pt:test` in `pt` folder