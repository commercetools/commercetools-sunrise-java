package com.commercetools.sunrise.controllers;

import com.commercetools.sunrise.common.SunriseVersionController;
import com.commercetools.sunrise.pt.WithSunriseApplication;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class SunriseVersionControllerTest extends WithSunriseApplication {

    @Test
    public void showsVersion() throws Exception {
        run(app(), VersionControllerTest.class, controller -> {
            final Result result = controller.show();
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
            assertThat(contentAsString(result)).contains("version").contains("build");
        });
    }

    private static class VersionControllerTest extends SunriseVersionController {

        public VersionControllerTest(final Application application) {
            super(application);
        }
    }
}