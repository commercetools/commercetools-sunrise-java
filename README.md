Sunrise Java Shop Framework :sunrise:
==============

[![Build Status](https://travis-ci.org/commercetools/commercetools-sunrise-java.png?branch=master)](https://travis-ci.org/commercetools/commercetools-sunrise-java) [![Stories in Ready](https://badge.waffle.io/commercetools/commercetools-sunrise-java.png?label=ready&title=Ready)](https://waffle.io/commercetools/commercetools-sunrise-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.commercetools.sunrise/commercetools-sunrise_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.commercetools.sunrise/commercetools-sunrise_2.11)

The next generation shop template.

* Demo: https://demo.commercetools.com
* [Documentation](manual/)
* [Javadoc](https://commercetools.github.io/commercetools-sunrise-java/javadoc/index.html)

## Integration tests against commercetools platform

* Setup your environment variables (use a test project), so you need not to put your shop credentials under version control:

```bash
export SUNRISE_IT_CTP_PROJECT_KEY="your-CTP-project-key"
export SUNRISE_IT_CTP_CLIENT_SECRET="your-CTP-client-secret"
export SUNRISE_IT_CTP_CLIENT_ID="your-CTP-client-id"
```
* `sbt it:test`

## Deployment

For an easy and fast deployment of your application we recommend [heroku](https://www.heroku.com):

<a href="https://heroku.com/deploy?template=https://github.com/commercetools/commercetools-sunrise-java/tree/v0.6.0"><img src="https://www.herokucdn.com/deploy/button.png" alt="Deploy"></a>

## Docker

[![Docker build](http://dockeri.co/image/sphereio/sunrise)](https://registry.hub.docker.com/u/sphereio/sunrise/)

You can also use a ready-to-run docker container.

```bash
$ docker run -it --rm -p 9000:9000 -e CTP_PROJECT_KEY=YOUR_PROJECT_KEY -e CTP_CLIENT_ID=YOUR_CLIENT_ID -e CTP_CLIENT_SECRET=YOUR_CLIENT_SECRET -e APPLICATION_SECRET=YOUR_PLAY_APPLICATION_SECRET sphereio/sunrise
```

## Related projects

### Sunrise Project Starter
The starting point to build your own online shop project

https://github.com/commercetools/commercetools-sunrise-java-starter

### Sunrise Theme
Handlebars templates + i18n messages + web assets

https://github.com/commercetools/commercetools-sunrise-theme

### Sunrise Data
Example data used on our demo

https://github.com/commercetools/commercetools-sunrise-data

### commercetools JVM SDK
SDK for JVM languages to communicate with comercetools projects

https://github.com/commercetools/commercetools-jvm-sdk
