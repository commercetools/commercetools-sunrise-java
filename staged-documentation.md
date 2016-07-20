## Overriding bean factories

When a bean does not contain all the data required for the template, you can override its factory to produce a subclass of the bean.

In this example shows how to add a field `lastUpdated` to the `ProductDetailPageContent`. This procedure is not limited to page class beans.

First override the bean to add the field including getters and setters:

```java
public class MyProductDetailPageContent extends ProductDetailPageContent {
    private String lastUpdated;

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
```

Then override the factory for the original bean:

```java
public class MyProductDetailPageContentFactory extends ProductDetailPageContentFactory {
    //here you can use dependency injection to get instances
    @Inject
    private UserContext userContext;

    //do not call here super.create because you would get a bean of the original class and not the subclass
    @Override
    public ProductDetailPageContent create(final ProductProjection product, final ProductVariant variant) {
        final MyProductDetailPageContent bean = new MyProductDetailPageContent();
        initialize(bean, product, variant);
        return bean;
    }

    //the new initialize method contains as first parameter the bean of you subclass and then the parameters of the create method
    protected final void initialize(final MyProductDetailPageContent bean, final ProductProjection product, final ProductVariant variant) {
        //use the initialize from the super class, don't forget the super, otherwise you get infinite recursion
        //also the super.initialize is final, don't try to override it
        super.initialize(bean, product, variant);

        //add here the new data
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(userContext.locale());
        bean.setLastUpdated(product.getLastModifiedAt().format(formatter));
    }
}
```

To register the new factory you need a Guice module to override the binding for the old factory.
You can reuse this module for multiple factories.
So it could look like:

```java
import com.commercetools.sunrise.productcatalog.productdetail.MyProductDetailPageContentFactory;
import com.commercetools.sunrise.productcatalog.productdetail.ProductDetailPageContentFactory;
import com.google.inject.AbstractModule;

public class FactoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ProductDetailPageContentFactory.class)//the default factory class for the creation
                .to(MyProductDetailPageContentFactory.class);//the class you want to use instead

        //add more bindings for factories here
    }
}

```

To make Play know the module, add it to `application.conf`:

```
play.modules.enabled += "absolute.path.to.your.FactoryModule"
```

## Handlebars View Components
## Handlebars adding helpers
## Logging the page data as JSON

```
<logger name="com.commercetools.sunrise.common.pages.SunrisePageDataJson" level="DEBUG" />
```

## Logging the requests to commercetools platform per web page