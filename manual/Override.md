# Overriding behaviour

## Overriding view model factories

When a view model does not contain all the data required for the template, you can override its factory to produce a subclass of the view model.

In the following example we will see how to add a field `lastUpdated` to the `ProductDetailPageContent`. Notice this procedure is not limited to `PageContent` class view models, but it can be used with any other view model class.

Let's first override the view model to add the desired field, including getters and setters:

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

Then we have to override the `ProductDetailPageContentFactory` so that it creates and initializes our view model instead:

```java
import java.util.Locale;

public class MyProductDetailPageContentFactory extends ProductDetailPageContentFactory {

    //here you can use dependency injection to get instances
    @Inject
    private Locale locale;

    //do not call here super.create because you would get a view model of the original class and not the subclass
    @Override
    public ProductDetailPageContent create(final ProductProjection product, final ProductVariant variant) {
        final MyProductDetailPageContent viewModel = new MyProductDetailPageContent();
        
        //this initializes our view model as the parent factory did 
        initialize(model, product, variant);

        //this initializes our view model with the new data
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale);
        viewModel.setLastUpdated(product.getLastModifiedAt().format(formatter));

        return viewModel;
    }
}
```

Now that we have finished changing the view model and its factory class, we only need to register the new factory so that it is used everywhere instead of the previous one. In order to achieve that, you need to override the binding for the old factory in a [Guice Module](https://google.github.io/guice/api-docs/latest/javadoc/index.html?com/google/inject/Module.html), as follows:

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

As always, make sure you have the Module enabled in `application.conf`:

```
play.modules.enabled += "absolute.path.to.your.FactoryModule"
```
