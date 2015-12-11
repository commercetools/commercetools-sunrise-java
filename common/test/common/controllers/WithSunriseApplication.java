package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.models.ProductDataConfig;
import common.templates.TemplateService;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.producttypes.MetaProductType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.F;
import play.test.WithApplication;

import java.util.*;
import java.util.concurrent.CompletionStage;

import static common.JsonUtils.readCtpObject;
import static java.util.Arrays.asList;
import static play.inject.Bindings.bind;

public abstract class WithSunriseApplication extends WithApplication {

    @Override
    protected Application provideApplication() {
        final Configuration configuration = baseConfiguration();
        final SphereClient sphereClient = injectedSphereClient();
        final PlayJavaSphereClient playJavaSphereClient = PlayJavaSphereClient.of(sphereClient);
        return new GuiceApplicationBuilder()
                .loadConfig(injectedConfiguration(configuration))
                .overrides(bind(SphereClient.class).toInstance(sphereClient))
                .overrides(bind(PlayJavaSphereClient.class).toInstance(playJavaSphereClient))
                .overrides(bind(TemplateService.class).toInstance(injectedTemplateService()))
                .overrides(bind(CmsService.class).toInstance(injectedCmsService()))
                .overrides(bind(ReverseRouter.class).toInstance(injectedReverseRouter()))
                .overrides(bind(ProjectContext.class).toInstance(injectedProjectContext()))
                .overrides(bind(CategoryTreeExtended.class).toInstance(injectedCategoryTree()))
                .overrides(bind(ProductDataConfig.class).toInstance(injectedProductDataConfig()))
                .build();
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
        return ProjectContext.of(asList(Locale.ENGLISH, Locale.GERMAN), asList(CountryCode.DE, CountryCode.US));
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

    protected Configuration injectedConfiguration(final Configuration configuration) {
        final Configuration testConfiguration = Configuration.load(new Environment(Mode.TEST));
        return configuration.withFallback(testConfiguration);
    }

    protected abstract <T> CompletionStage<T> fakeSphereClientResponse(final SphereRequest<T> request);

    protected final SphereClient injectedSphereClient() {
        return new SphereClient() {

            @Override
            public <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
                return fakeSphereClientResponse(sphereRequest);
            }

            @Override
            public void close() {

            }

            @Override
            public String toString() {
                return "SphereClientTestDouble";
            }
        };
    }

    private Configuration baseConfiguration() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);
        return new Configuration(additionalSettings);
    }
}