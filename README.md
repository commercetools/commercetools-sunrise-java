![SPHERE.IO icon](https://admin.sphere.io/assets/images/sphere_logo_rgb_long.png)

sphere-sunrise :sunrise:
==============

[![Build Status](https://travis-ci.org/sphereio/sphere-sunrise.png?branch=master)](https://travis-ci.org/sphereio/sphere-sunrise) [![Stories in Ready](https://badge.waffle.io/sphereio/sphere-sunrise.png?label=ready&title=Ready)](https://waffle.io/sphereio/sphere-sunrise)

The next generation shop template.

:warning: _**Notice this template is in a very early stage of development.**_

_Unfortunately we do not currently have any finished template that uses our last version of the [SPHERE JVM SDK](https://github.com/sphereio/sphere-jvm-sdk). If you are looking for a more complete project you can check our [Fedora](https://github.com/commercetools/sphere-fedora) template, but be aware that this project uses the older version [SPHERE Play SDK](https://github.com/commercetools/sphere-play-sdk)._

* Demo: https://sunrise.sphere.io
* [Javadoc](https://sphereio.github.io/sphere-sunrise/javadoc/index.html)

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

* open <a href="http://localhost:9000">http://localhost:9000</a> in your browser and set the SPHERE.IO project credentials

## Integration tests against SPHERE.IO backend

* Setup your environment variables (use a test project), so you need not to put your shop credentials under version control:

```bash
export SPHERE_SUNRISE_IT_PROJECT="your-project-key"
export SPHERE_SUNRISE_IT_CLIENT_SECRET="your-the-client-secret"
export SPHERE_SUNRISE_IT_CLIENT_ID="your-client-id"
```
* `sbt it:test`

## Deployment

For an easy and fast deployment of your application we recommend [heroku](https://www.heroku.com):

<a href="https://heroku.com/deploy?template=https://github.com/sphereio/sphere-sunrise"><img src="https://www.herokucdn.com/deploy/button.png" alt="Deploy"></a>

## Related projects
* https://github.com/sphereio/sphere-sunrise-scenarios
* https://github.com/sphereio/sphere-sunrise-design
* https://github.com/sphereio/sphere-jvm-sdk
