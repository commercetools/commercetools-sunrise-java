General guidelines about how to successfully develop an application based on [SPHERE JVM SDK](https://github.com/sphereio/sphere-jvm-sdk) with [Play Framework](https://www.playframework.com/).

###Controllers
Controllers should do all HTTP related stuff.

#####Takes care of
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

