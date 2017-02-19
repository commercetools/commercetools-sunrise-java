package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContentFactory;
import com.commercetools.sunrise.pt.WithSunriseApplication;
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

            }
        };
        return appBuilder(module).build();
    }

    private static class HomeTestController extends SunriseHomeController {

        public HomeTestController(final TemplateRenderer templateRenderer, final HomePageContentFactory homePageContentFactory) {
            super(x -> {}, templateRenderer, homePageContentFactory);
        }
    }
}