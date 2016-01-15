package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import common.templates.TemplateService;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.producttypes.MetaProductType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.apache.commons.lang3.RandomStringUtils;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;

import javax.money.Monetary;
import java.util.*;

import static common.JsonUtils.readCtpObject;
import static java.util.Arrays.asList;
import static play.inject.Bindings.bind;
import static play.test.Helpers.running;

public abstract class WithSunriseApplication {
    public static final int ALLOWED_TIMEOUT = 1000;

    @FunctionalInterface
    public interface CheckedConsumer<T> {

        void apply(T t) throws Exception;
    }

    protected final <C extends Controller> void run(final Application app, final Class<C> controllerClass,
                                                    final CheckedConsumer<C> test) {
        running(app, () -> {
            try {
                test.apply(app.injector().instanceOf(controllerClass));
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    protected final void setContext(final Http.Request request) {
        Http.Context.current.set(new Http.Context(request));
    }

    protected GuiceApplicationBuilder applicationBuilder(final SphereClient sphereClient) {
        final PlayJavaSphereClient playJavaSphereClient = PlayJavaSphereClient.of(sphereClient);
        return new GuiceApplicationBuilder()
                .in(Environment.simple())
                .loadConfig(injectedConfiguration(baseConfiguration()))
                .overrides(bind(SphereClient.class).toInstance(sphereClient))
                .overrides(bind(PlayJavaSphereClient.class).toInstance(playJavaSphereClient))
                .overrides(bind(I18nResolver.class).toInstance(injectedI18nResolver()))
                .overrides(bind(TemplateService.class).toInstance(injectedTemplateService()))
                .overrides(bind(CmsService.class).toInstance(injectedCmsService()))
                .overrides(bind(ReverseRouter.class).toInstance(injectedReverseRouter()))
                .overrides(bind(ProjectContext.class).toInstance(injectedProjectContext()))
                .overrides(bind(CategoryTreeExtended.class).toInstance(injectedCategoryTree()))
                .overrides(bind(ProductDataConfig.class).toInstance(injectedProductDataConfig()));
    }

    protected Http.RequestBuilder requestBuilder() {
        return new Http.RequestBuilder().id(1L);
    }

    protected Configuration baseConfiguration() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);
        additionalSettings.put("play.crypto.secret", RandomStringUtils.randomAlphanumeric(15));
        return new Configuration(additionalSettings);
    }

    protected Configuration injectedConfiguration(final Configuration configuration) {
        final Configuration testConfiguration = Configuration.load(new Environment(Mode.TEST));
        return configuration.withFallback(testConfiguration);
    }

    private I18nResolver injectedI18nResolver() {
        return ((locale, bundle, key, args) -> Optional.empty());
    }

    protected TemplateService injectedTemplateService() {
        return ((templateName, pageData, locales) -> "");
    }

    protected CmsService injectedCmsService() {
        return ((locale, pageKey) -> F.Promise.pure((messageKey, args) -> Optional.empty()));
    }

    protected ReverseRouter injectedReverseRouter() {
        return new TestableReverseRouter();
    }

    protected ProjectContext injectedProjectContext() {
        return ProjectContext.of(
                asList(Locale.ENGLISH, Locale.GERMAN),
                asList(CountryCode.US, CountryCode.DE),
                asList(Monetary.getCurrency("USD"), Monetary.getCurrency("EUR")));
    }

    protected CategoryTreeExtended injectedCategoryTree() {
        final PagedQueryResult<Category> result = readCtpObject("data/categories.json", CategoryQuery.resultTypeReference());
        return CategoryTreeExtended.of(result.getResults());
    }

    protected ProductDataConfig injectedProductDataConfig() {
        final PagedQueryResult<ProductType> result = readCtpObject("data/product-types.json", ProductTypeQuery.resultTypeReference());
        final MetaProductType metaProductType = MetaProductType.of(result.getResults());
        return ProductDataConfig.of(metaProductType, asList("foo", "bar"));
    }
}