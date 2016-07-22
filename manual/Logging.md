# Logging

## Logging the PageData as JSON

To log the `PageData` given to the template engine, add this to your `logback.xml`:

```xml
<logger name="com.commercetools.sunrise.common.pages.SunrisePageDataJson" level="DEBUG" />
```

Check this [example output](https://gist.github.com/schleichardt/5e8995bbf8a18f155ae01ceabf9d4765).

## Logging the requests to commercetools platform per web page

To log the requests to the commercetools platform used to render a single shop page, add this to your `logback.xml`:

```xml
<logger name="sphere.metrics.simple" level="trace" />
```

It will output something similar to:

```
TRACE sphere.metrics.simple - commercetools requests in GET /en/women-shoes-sneakers: 
  POST /product-projections/search 86ms 1387102bytes
  POST /carts 54ms 507bytes
```
