package demo;

import com.commercetools.sunrise.common.DefaultProductCatalogTestModule;
import com.commercetools.sunrise.common.DefaultTestModule;
import com.commercetools.sunrise.common.WithSunriseApplication;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class HomePageControllerTest extends WithSunriseApplication {

    @Test
    public void homeIsAlive() {
        setContext(requestBuilder().build());
        run(application(), HomePageController.class, controller -> {
            final Result result = controller.show("de").toCompletableFuture().join();
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(contentAsString(result)).isNullOrEmpty();
        });
    }

    private Application application() {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(ProductListBeanFactory.class).toInstance(new ProductListBeanFactory());
            }
        };
        return appBuilder(module).build();
    }

    @Override
    protected DefaultTestModule defaultModule() {
        return new DefaultProductCatalogTestModule();
    }
}