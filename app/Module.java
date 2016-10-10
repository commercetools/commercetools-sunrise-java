import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.categorytree.CategoryTreeInNewProvider;
import com.commercetools.sunrise.common.categorytree.RefreshableCategoryTreeProvider;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.commercetools.sunrise.common.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.DefaultPageNavMenuControllerComponent;
import com.commercetools.sunrise.common.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.common.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.commercetools.sunrise.shoppingcart.MiniCartControllerComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutCommonComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutStepWidgetComponent;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Singleton;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(CmsService.class).toProvider(FileBasedCmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(HandlebarsTemplateEngineProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(ConfigurableI18nResolverProvider.class).in(Singleton.class);
        bind(HttpAuthentication.class).toProvider(BasicAuthenticationProvider.class).in(Singleton.class);
        bind(CategoryTree.class).toProvider(RefreshableCategoryTreeProvider.class).in(Singleton.class);
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toProvider(CategoryTreeInNewProvider.class).in(Singleton.class);
    }

    @Provides
    public MultiControllerComponentResolver provideMultiControllerComponentResolver() {
        //here are also instanceof checks possible
        return new MultiControllerComponentResolverBuilder()
                .add(CheckoutCommonComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(CheckoutStepWidgetComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(MiniCartControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(DefaultPageNavMenuControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(LocationSelectorControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .build();
    }
}
