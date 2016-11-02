package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.DefaultTestModule;
import com.commercetools.sunrise.common.WithSunriseApplication;
import com.commercetools.sunrise.common.controllers.TestableReverseRouter;
import com.commercetools.sunrise.common.controllers.WebJarAssetsReverseRouter;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.productcatalog.ProductCatalogTestModule;
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
                final TestableReverseRouter reverseRouter = reverseRouter();
                bind(WebJarAssetsReverseRouter.class).toInstance(reverseRouter);
                bind(HomeReverseRouter.class).toInstance(reverseRouter);
                bind(Http.Context.class).toInstance(Http.Context.current());
            }
        };
        return appBuilder(module).build();
    }

    @Override
    protected DefaultTestModule defaultModule() {
        return new ProductCatalogTestModule();
    }

    private static TestableReverseRouter reverseRouter() {
        final TestableReverseRouter reverseRouter = new TestableReverseRouter();
        reverseRouter.setThemeAssetsUrl("assets");
        reverseRouter.setShowHomeUrl("/");
        return reverseRouter;
    }

    private static class HomeTestController extends SunriseHomeController {

    }
}