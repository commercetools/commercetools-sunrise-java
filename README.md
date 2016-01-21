Sunrise :sunrise:
==============

[![Build Status](https://travis-ci.org/sphereio/commercetools-sunrise-java.png?branch=master)](https://travis-ci.org/sphereio/commercetools-sunrise-java) [![Stories in Ready](https://badge.waffle.io/sphereio/commercetools-sunrise-java.png?label=ready&title=Ready)](https://waffle.io/sphereio/commercetools-sunrise-java?source=sphereio%2Fcommercetools-sunrise-java)

The next generation shop template.

* Demo: https://sunrise.sphere.io
* [Javadoc](https://sphereio.github.io/commercetools-sunrise-java/javadoc/index.html)

## Preconditions

* install [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* install [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html), Mac/Linux users can use the SBT script in the base folder (use `./sbt` instead of `sbt` in commands)

## Run it locally

* on Linux/Mac: `./activator ~run` 
* on Windows: `activator ~run`

The output will be like

```
[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000
(Server started, use Ctrl+D to stop and go back to the console...)
```

* open <a href="http://localhost:9000">http://localhost:9000</a> in your browser and set the commercetools platform project credentials

## Integration tests against commercetools platform

* Setup your environment variables (use a test project), so you need not to put your shop credentials under version control:

```bash
export SPHERE_SUNRISE_IT_PROJECT="your-project-key"
export SPHERE_SUNRISE_IT_CLIENT_SECRET="your-the-client-secret"
export SPHERE_SUNRISE_IT_CLIENT_ID="your-client-id"
```
* `sbt it:test`

## Deployment

For an easy and fast deployment of your application we recommend [heroku](https://www.heroku.com):

<a href="https://heroku.com/deploy?template=https://github.com/sphereio/commercetools-sunrise-java"><img src="https://www.herokucdn.com/deploy/button.png" alt="Deploy"></a>

## Docker

[![Docker build](http://dockeri.co/image/sphereio/sunrise)](https://registry.hub.docker.com/u/sphereio/sunrise/)

You can also use a ready-to-run docker container.

```bash
$ docker run -it --rm -p 9000:9000 sphereio/sunrise
```

## Related projects

* https://github.com/sphereio/sphere-sunrise-data
* https://github.com/sphereio/sphere-sunrise-scenarios
* https://github.com/sphereio/commercetools-sunrise-design
* https://github.com/sphereio/sphere-jvm-sdk

