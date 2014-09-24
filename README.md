![SPHERE.IO icon](https://admin.sphere.io/assets/images/sphere_logo_rgb_long.png)

sphere-sunrise
==============

[![Build Status](https://travis-ci.org/sphereio/sphere-sunrise.png?branch=master)](https://travis-ci.org/sphereio/sphere-sunrise)

The next generation shop template. Currently under initial development.

* Demo: https://sunrise.sphere.io
* [Javadoc](https://sphereio.github.io/sphere-sunrise/javadoc/index.html)
* [Test Coverage Report](https://sphereio.github.io/sphere-sunrise/coverage/index.html)

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
* `sbt console` creates an interactive Scala console where you can experiment, use Ctrl d to exit.

example session:

```
sbt console
[info] Loading project definition from [...]
[info] Set current project to sphere-sunrise (in build file:[...])
[info] Compiling 1 Java source to [...]
[info] Starting scala interpreter...
[info]
Use 'val client = createClient()' to create a client instance.
import tutorial.ClientFactory.createClient
import io.sphere.sdk.categories._
Welcome to Scala version 2.10.4 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_05).
Type in expressions to have them evaluated.
Type :help for more information.

scala> val client = createClient()
client: io.sphere.sdk.client.PlayJavaClient = io.sphere.sdk.client.PlayJavaClientImpl@467e33e7

scala> val promise = client.execute(Categories.query())
promise: play.libs.F.Promise[io.sphere.sdk.client.PagedQueryResult[io.sphere.sdk.categories.Category]] = play.libs.F$Promise@39c6382c

scala> val result = promise.get(1000)
result: io.sphere.sdk.client.PagedQueryResult[io.sphere.sdk.categories.Category] = PagedQueryResponse{offset=0, count=4, total=4, results=[CategoryImpl{id=5ebe6dc9-ba32-4030-9f3e-eee0137a1274, version=1, createdAt=2014-06-06T11:12:05.520+02:00, lastModifiedAt=2014-06-06T11:12:05.520+02:00, name=LocalizedString(en -> Snowboard equipment), slug=LocalizedString(en -> snowboard-equipment), description=Optional.absent(), ancestors=, parent=Optional.absent(), orderHint=Optional.of(0.000014020459255201865700631), children=[], pathInTree=}, CategoryImpl{id=3621ef75-adc4-4a31-8a3b-3753b1c0a209, version=1, createdAt=2014-06-06T11:12:05.531+02:00, lastModifiedAt=2014-06-06T11:12:05.531+02:00, name=LocalizedString(en -> Tank tops), slug=LocalizedString(en -> tank-tops), description=Optional.absent(...
```

## Deployment

For an easy and fast deployment of your application we recommend [heroku](https://www.heroku.com):

<a href="https://heroku.com/deploy?template=https://github.com/sphereio/sphere-sunrise"><img src="https://www.herokucdn.com/deploy/button.png" alt="Deploy"></a>
