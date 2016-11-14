Sunrise Java Shop Framework :sunrise:
==============

[![Build Status](https://travis-ci.org/commercetools/commercetools-sunrise-java.png?branch=master)](https://travis-ci.org/commercetools/commercetools-sunrise-java) [![Stories in Ready](https://badge.waffle.io/commercetools/commercetools-sunrise-java.png?label=ready&title=Ready)](https://waffle.io/commercetools/commercetools-sunrise-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.commercetools.sunrise/commercetools-sunrise_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.commercetools.sunrise/commercetools-sunrise_2.11)

The next generation shop framework. 

* [Demo](https://demo.commercetools.com)
* [Documentation](manual/)
* [Javadoc](https://commercetools.github.io/commercetools-sunrise-java/javadoc/index.html)

## Installation

### Sunrise Starter Project
The recommended way to start using Sunrise is to clone the [Sunrise Starter Project](https://github.com/commercetools/commercetools-sunrise-java-starter) and use it as a template project. It already contains all dependencies (i.e. Sunrise Framework and Theme) and configurations needed to run your Sunrise-based project.

### Starting from scratch
Alternatively you can start your own Play Framework project and configure it yourself, using Sunrise as dependency:
```sbt
val sunriseFrameworkVersion = "0.14.0" // or desired version
libraryDependencies ++= Seq(
  // add Sunrise Framework dependencies as needed, e.g.:
  "com.commercetools.sunrise" %% "product-catalog" % sunriseFrameworkVersion,
  "com.commercetools.sunrise" %% "shopping-cart" % sunriseFrameworkVersion,
  "com.commercetools.sunrise" %% "my-account" % sunriseFrameworkVersion,
  // add the desired Sunrise Theme as dependency, e.g.:
  "com.commercetools.sunrise" % "commercetools-sunrise-theme" % "0.61.1"
)
```
These dependencies provide default Controllers which can be enabled by extending them into your own Controller and registering a route for it.

For example, if we wanted to enable an endpoint to see the contents of the cart, we would simply extend the `SunriseCartDetailController`:
```java
@RequestScoped
public class CartDetailController extends SunriseCartDetailController {
  // here you can register components or override methods to change behaviour
}
```
Then we only need to associate our `CartDetailController` to a route with the desired pattern in `conf/routes`:
```scala
# Shows the details of the cart belonging to the current session
GET  /:languageTag/cart       @shoppingcart.CartDetailController.show(languageTag: String)
```
Accessing [http://localhost:9000/en/cart](http://localhost:9000/en/cart) should now allow us to see the contents of our cart.

Check [Sunrise Starter Project](https://github.com/commercetools/commercetools-sunrise-java-starter) to adjust any other required configuration.

## Integration tests against commercetools platform

* Setup your environment variables (use a test project), so you need not to put your shop credentials under version control:

```bash
export SUNRISE_IT_CTP_PROJECT_KEY="your-CTP-project-key"
export SUNRISE_IT_CTP_CLIENT_SECRET="your-CTP-client-secret"
export SUNRISE_IT_CTP_CLIENT_ID="your-CTP-client-id"
```
* `sbt it:test`

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
