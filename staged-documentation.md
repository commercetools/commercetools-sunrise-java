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

Remember to add the Module to `application.conf` to enable it in Play:

```
play.modules.enabled += "absolute.path.to.your.FactoryModule"
```



## Handlebars View Components

The demo theme contains a [call to "components-include"](https://github.com/commercetools/commercetools-sunrise-theme/blob/master/input/templates/partials/common/footer.hbs#L1),
which contains a [list of components](https://github.com/commercetools/commercetools-sunrise-theme/blob/master/input/templates/partials/components/components-include.hbs).
A data item for this could look like:

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

In the following example we create a component that shows a banner to summer campaign.

First we create a `ControllerComponent` that adds the handlebars component to the page data:

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
        //the template is in conf/templates/components/summercampaign/banner.hbs or in your theme project
        component.setTemplateName("components/summercampaign/banner");//without .hbs!!!
        Map<String, Object> data = new HashMap<>();
        data.put("src", configuration.getString("summercampaign.img.src"));
        data.put("width", configuration.getString("summercampaign.img.width"));
        data.put("height", configuration.getString("summercampaign.img.height"));
        data.put("href", demo.productcatalog.routes.SummerCampaignController.show(userContext.languageTag()).url());
        component.setComponentData(data);
        return component;
    }
}
```

You still need to wire the `ControllerComponent` to a controller or multiple controllers.
Since it make sense to put it to all controllers except for checkout, we need a Guice Module:

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
                .build();
    }
}
```

The `ComponentsModule` can be reused for multiple components, just add a `add` line for building the `MultiControllerComponentResolver`.

Don't forget to register the module in `application.conf` with

```
play.modules.enabled += "absolute.path.to.your.ComponentsModule"
```

For `conf/templates/components/summercampaign/banner.hbs` we can use

```handlebars
<div class="row text-center">
    <a href="{{componentData.href}}">
        <p class="home-suggestions-title">Summer is here - buy sunglasses</p>
        <img src="{{componentData.src}}" width="{{componentData.width}}" height="{{componentData.height}}" />
    </a>
</div>
<hr class="home-suggestions-hr" />
```

And then it looks like
![result](documentation-images/summercampaign-sungrasses-home.png)


## Handlebars adding helpers

We strongly suggest not to add Handlebars helpers to enable calculations or any kind of business logic in the templates. Handlebars helpers are only meant to simplify presentation logic (in contrast of a pure logic-less template such as [Mustache](https://mustache.github.io/)). Using helpers for other purposes would defeat the logic-less approach that we try to promote.

Suppose you want to add a helper `hello` which can be called like this:

```handlebars
{{hello "user"}}
```

And is supposed to render this:

```html
hello, user
```

You can write a helper like this:

```java
public final class ShopHandlebarsHelpers {
    private ShopHandlebarsHelpers() {
    }

    public static String hello(final Object name) {
        return "hello, " + Optional.ofNullable(name).orElse("world");
    }
}
```

To register it to Sunrise you need to override the `HandlebarsFactory`:

```java
public class ShopHandlebarsFactory extends HandlebarsFactory {
    @Override
    public Handlebars create() {
        final Handlebars handlebars = super.create();
        handlebars.registerHelpers(ShopHandlebarsHelpers.class);
        return handlebars;
    }
}
```

and then bind the factory in a module:

```java
public class FactoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HandlebarsFactory.class).to(ShopHandlebarsFactory.class);

        //add more bindings here for factories
    }
}
```

To make Play know the module, add it to `application.conf`:

```
play.modules.enabled += "absolute.path.to.your.FactoryModule"
```

See also [creating Handlebars.java helpers](http://jknack.github.io/handlebars.java/helpers.html) and [built-in helpers in Handlebars.java](https://github.com/jknack/handlebars.java#built-in-helpers).

## Logging the page data as JSON

To log the page data which is given to the template engine, add this to `logback.xml`:

```xml
<logger name="com.commercetools.sunrise.common.pages.SunrisePageDataJson" level="DEBUG" />
```

An example output can be found [here](https://gist.github.com/schleichardt/5e8995bbf8a18f155ae01ceabf9d4765).

## Logging the requests to commercetools platform per web page

To log the requests to the commercetools platform which are used to render one shop page, add this to `logback.xml`:

```xml
<logger name="sphere.metrics.simple" level="trace" />
```

This is how it looks like:

```
TRACE sphere.metrics.simple - commercetools requests in GET /en/women-shoes-sneakers:
     POST /product-projections/search 1484ms 1387102bytes
```
