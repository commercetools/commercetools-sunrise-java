![SPHERE.IO icon](https://admin.sphere.io/assets/images/sphere_logo_rgb_long.png)

sphere-sunrise
==============

[![Build Status](https://travis-ci.org/sphereio/sphere-sunrise.png?branch=master)](https://travis-ci.org/sphereio/sphere-sunrise)

The next generation shop template. Currently under initial development.

* Demo: https://sunrise.sphere.io

## Preconditions

* install [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* install [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html), Mac/Linux users can use the SBT script in the base folder (use `./sbt` instead of `sbt` in commands)

## Run it locally

* Setup your environment variables, so you need not to put your shop credentials under version control:

```bash
export SPHERE_SUNRISE_PROJECT="your-project-key"
export SPHERE_SUNRISE_CLIENT_SECRET="your-the-client-secret"
export SPHERE_SUNRISE_CLIENT_ID="your-client-id"
```

* You can store the variables in `~/.bash_profile` (Mac) or `~/.bashrc` (Linux) so the next time you open a console they are automatically available.
* `sbt ~run`

The output will be like

```
[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Ctrl+D to stop and go back to the console...)
```

* open http://localhost:9000 in your browser

## Integration tests against SPHERE.IO backend

* Setup your environment variables (use a test project), so you need not to put your shop credentials under version control:

```bash
export SPHERE_SUNRISE_IT_TESTS_PROJECT="your-project-key"
export SPHERE_SUNRISE_IT_TESTS_CLIENT_SECRET="your-the-client-secret"
export SPHERE_SUNRISE_IT_TESTS_CLIENT_ID="your-client-id"
```
* `sbt it:test`


## Special Commands

* `sbt cover` executes all tests (unit, integration) and creates test reports in `target/scala-2.10/it-jacoco/html/index.html`
