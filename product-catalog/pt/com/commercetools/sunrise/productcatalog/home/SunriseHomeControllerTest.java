package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.productcatalog.ProductCatalogTestModule;
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

public class SunriseHomeControllerTest extends WithSunriseApplication {

    @Test
    public void homeIsAlive() {
        setContext(requestBuilder().build());
        run(application(), HomeTestController.class, controller -> {
            final Result result = controller.show("de").toCompletableFuture().join();
            assertThat(result.status()).isEqualTo(Http.Status.OK);
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
        return new ProductCatalogTestModule();
    }

    private static class HomeTestController extends SunriseHomeController {

    }
}