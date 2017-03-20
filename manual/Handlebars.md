# Handlebars

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
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.components.ViewModelComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public final class SummerCampaignControllerComponent extends Base implements ControllerComponent, PageDataHook {

    @Inject
    private Configuration configuration;
    @Inject
    private Locale locale;

    @Override
    public void acceptPageData(final PageData pageData) {
        final ComponentViewModel component = createComponentViewModel();
        pageData.getContent().addComponent(component);
    }

    private ComponentViewModel createComponentViewModel() {
        final ComponentViewModel component = new ComponentViewModel();
        component.setTemplateName("components/summercampaign/banner"); //template path without .hbs!!!
        component.setComponentData(createComponentData());
        return component;
    }

    private Map<String, Object> createComponentData() {
        Map<String, Object> data = new HashMap<>();
        data.put("src", configuration.getString("summercampaign.img.src"));
        data.put("width", configuration.getString("summercampaign.img.width"));
        data.put("height", configuration.getString("summercampaign.img.height"));
        data.put("href", demo.productcatalog.routes.SummerCampaignController.show(locale.toLanguageTag()).url());
        return data;
    }
}
```

We still need to wire the `ControllerComponent` to the desired controllers. Let's say that we want to display it on the home page.
 
```java
import demo.productcatalog.SummerCampaignControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;

import com.commercetools.sunrise.hooks.RegisteredComponents;

@RegisteredComponents(SummerCampaignControllerComponent.class)
public class HomeController extends SunriseHomeController {
    // body of the controller
}
```

If everything went right, the home page should have a banner like this:

![result](images/summercampaign-sungrasses-home.png)

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

