General guidelines about how to successfully develop an application based on [SPHERE JVM SDK](https://github.com/sphereio/sphere-jvm-sdk) with [Play Framework](https://www.playframework.com/).

###Controllers
Controllers should do all HTTP related stuff.

#####Take care of
- HTTP requests and responses.
- Cookies and sessions.
- Forms.
- Context and other Play lifecycle specific stuff.
- Templates.

#####Patterns and rules
- Dependency injection of services.
- Simple code.
- Should end with the word "Controller".
- Most likely does not have mutable state.
- Separate controller classes according to login required or not, then use controller-wide annotations.

#####How do tests look like
- Check cookies and sessions.
- Check response code.
- Check certain words in response content.
- Use `callAction` with `fakeRequest` from Play.
- Extend test classes like `WithApplication` instead of creating an anonymous runnable in every test method.

###Services
Services request domain related stuff, usually to external services.

#####Take care of
- Sphere and any other service needed.
- Communication with external systems.
- No access to HTML templates, except for email templates.

#####Patterns and rules
- Asynchronous calls for external services.
- Interface with implementation to mock external result.
- May be reused.
- May end with with the term "Service".
- Should be thread-safe.
- Most likely doesn't have mutable members.

#####How do tests look like
- Should never use running application.
- Use mocking for external system.

###Models
Models hold local data containers and domain logic.

#####Take care of
- Only aware of other models.

#####Patterns and rules
- Immutable objects.
- Highly reused.
- Created by static factory methods or builders.
- Should not end with the term "Model".

#####How do tests look like
- Should never use running application.
- No dependency injection should be necessary.

###Views
Views generate pages with data provided by controller “as is”, with almost no logic.

#####Take care of
- Template parameters.
- Internal URLs by reverse routing.
- Optionally request, session or context (but harder to test).
 
#####Patterns and rules
- `Optional` class instances, to easily generate default messages.

#####How do tests look like
- Without running application (in Play 2.2.x is not every time possible).
- No dependency injection should be necessary.

###Utils
Util classes hold common re-used methods among different classes.

#####Take care of
- Other util classes, if any.

#####Patterns and rules
- Final classes with static methods and private constructors.
- No observable state changes.
- No instance members.

#####How do tests look like
- Without running application.

###In general
- Unit tests in `test` folder, integration tests in `it` folder.
- Use new Java 7 features:
 - [`AutoCloseable`](http://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html) and [`Closeable`](http://docs.oracle.com/javase/8/docs/api/java/io/Closeable.html) interfaces
 - [Type inference](http://docs.oracle.com/javase/tutorial/java/generics/genTypeInference.html)
 - [`try-with-resources`](http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)
 - [Multicatch Exceptions](http://docs.oracle.com/javase/8/docs/technotes/guides/language/catch-multiple.html)
 - [`Objects`](http://docs.oracle.com/javase/8/docs/api/java/util/Objects.html) class methods, e.g. `equals`, `hashCode` or `requireNonNull`.
- Take special care when naming classes or methods.
 - Never use "helper" in the name.
- Use fully qualified class names when using Scala classes that clashes with JDK standard classes (e.g. use `scala.collection.mutable.StringBuilder`, as it clashes with `java.lang.StringBuilder`).
- Only self written code should exist in the GIT repository, any external JavaScript and CSS should be loaded with Webjars.
- Do not hesitate to use `*` for imports, especially for `import static play.test.Helpers.*;`.
- Java8 streams.

