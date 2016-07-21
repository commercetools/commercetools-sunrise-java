## Overriding bean factories

When a bean does not contain all the data required for the template, you can override its factory to produce a subclass of the bean.

In the following example we will see how to add a field `lastUpdated` to the `ProductDetailPageContent`. Notice this procedure is not limited to `PageContent` class beans, but it can be used with any other bean class.

Let's first override the bean to add the desired field, including getters and setters:

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

Then we have to override the `ProductDetailPageContentFactory` so that it creates and initializes our bean instead:

```java
public class MyProductDetailPageContentFactory extends ProductDetailPageContentFactory {

    //here you can use dependency injection to get instances
    @Inject
    private UserContext userContext;

    //do not call here super.create because you would get a bean of the original class and not the subclass
    @Override
    public ProductDetailPageContent create(final ProductProjection product, final ProductVariant variant) {
        final MyProductDetailPageContent bean = new MyProductDetailPageContent();
        
        //this initializes our bean as the parent factory did 
        initialize(bean, product, variant);

        //this initializes our bean with the new data
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(userContext.locale());
        bean.setLastUpdated(product.getLastModifiedAt().format(formatter));

        return bean;
    }
}
```

Now that we have finished changing the bean and its factory class, we only need to register the new factory so that it is used everywhere instead of the previous one. In order to achieve that, you need to override the binding for the old factory in a [Guice Module](https://google.github.io/guice/api-docs/latest/javadoc/index.html?com/google/inject/Module.html), as follows:

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



## Handlebars View Components

View Components enables you to include additional templates to the theme without actually modifying the theme, just by providing the desired templates. Currently the usage in the default Theme is limited to inserting elements before the footer, but you can use it anywhere.  

Just include in your template the partial [`components-include.hbs`](https://github.com/commercetools/commercetools-sunrise-theme/blob/master/input/templates/partials/components/components-include.hbs) (as it is done in [`footer.hbs`](https://github.com/commercetools/commercetools-sunrise-theme/blob/master/input/templates/partials/common/footer.hbs#L1)). This template calls the given list of components by its template name. The required template data structure should have the `templateName` and `componentData`, which contains arbitrary data.
 
As an example, let's create a component that shows a banner to a summer campaign with the following data:

```json
[
  {
    "templateName": "components/summercampaign/banner",
    "componentData": {
      "src": "https://sunrise.commercetools.com/assets/img/cms/woman_large-8to3.jpg",
      "width": 768,
      "height": 288,
      "href": "http://localhost:9000/summer/en"
    }
  }
]
```

We will use the following template, located in `conf/templates/components/summercampaign/banner.hbs`:

```handlebars
<div class="row text-center">
    <a href="{{componentData.href}}">
        <p class="home-suggestions-title">Summer is here - buy sunglasses</p>
        <img src="{{componentData.src}}" width="{{componentData.width}}" height="{{componentData.height}}" />
    </a>
</div>
<hr class="home-suggestions-hr" />
```

First we need to create a `ControllerComponent` that adds the View Component to the `PageData`:

```java
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.components.ComponentBean;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public final class SummerCampaignControllerComponent extends Base implements ControllerComponent, PageDataHook {

    @Inject
    private Configuration configuration;
    @Inject
    private UserContext userContext;

    @Override
    public void acceptPageData(final PageData pageData) {
        final ComponentBean component = createComponentBean();
        pageData.getContent().addComponent(component);
    }

    private ComponentBean createComponentBean() {
        final ComponentBean component = new ComponentBean();
        component.setTemplateName("components/summercampaign/banner"); //template path without .hbs!!!
        component.setComponentData(createComponentData());
        return component;
    }

    private Map<String, Object> createComponentData() {
        Map<String, Object> data = new HashMap<>();
        data.put("src", configuration.getString("summercampaign.img.src"));
        data.put("width", configuration.getString("summercampaign.img.width"));
        data.put("height", configuration.getString("summercampaign.img.height"));
        data.put("href", demo.productcatalog.routes.SummerCampaignController.show(userContext.languageTag()).url());
        return data;
    }
}
```

We still need to wire the `ControllerComponent` to the desired controllers. Let's say that we want to display it on all pages except for the checkout, for that we need to build a `MultiControllerComponentResolver` with our Controller Component included, which is then injected via Dependency Injection.
 
Check the following Guice Module as an example:

```java
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import demo.productcatalog.SummerCampaignControllerComponent;

public class ComponentsModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public MultiControllerComponentResolver multiControllerComponentResolver() {
        return new MultiControllerComponentResolverBuilder()
                .add(SummerCampaignControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                //add more controller components here
                .build();
    }
}
```

As always, make sure you have the Module enabled in `application.conf`:

```
play.modules.enabled += "absolute.path.to.your.ComponentsModule"
```

If everything went right, all pages except the checkout pages should have a banner like this:

![result](documentation-images/summercampaign-sungrasses-home.png)


## Adding Helpers to Handlebars

> We strongly suggest not to add Handlebars helpers to enable calculations or any kind of business logic in the templates. Handlebars helpers are only meant to simplify presentation logic (in contrast to a pure logic-less template engine such as [Mustache](https://mustache.github.io/)). Using helpers for other purposes would defeat the logic-less approach we try to promote.

Let's suppose we want to add a helper `hello` to display a salutation with the user's name. From a template, we can call this helper as follows:

```handlebars
{{hello "user"}}
```

Which should render:

```html
hello, user
```

In the Sunrise application we can implement the helper like this:

```java
public final class ShopHandlebarsHelpers {

    private ShopHandlebarsHelpers() {
    }

    public static String hello(final Object name) {
        return "hello, " + Optional.ofNullable(name).orElse("world");
    }
}
```

In order to register it to the `HandlebarsTemplateEngine`, we need to override the `HandlebarsFactory` to additionally register our new helper:

```java
public class ShopHandlebarsFactory extends HandlebarsFactory {

    @Override
    public Handlebars create() {
        final Handlebars handlebars = super.create();
        handlebars.registerHelpers(ShopHandlebarsHelpers.class);
        //register more helpers here
        return handlebars;
    }
}
```

And finally we just bind the factory to replace the previous one:

```java
public class FactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HandlebarsFactory.class).to(ShopHandlebarsFactory.class);

        //add more bindings here for factories
    }
}
```

As always, make sure you have the Module enabled in `application.conf`:

```
play.modules.enabled += "absolute.path.to.your.FactoryModule"
```

See also [how to create helpers](http://jknack.github.io/handlebars.java/helpers.html) and check the existing [built-in helpers](https://github.com/jknack/handlebars.java#built-in-helpers) in Handlebars.java.

## Logging

### Logging the PageData as JSON

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
