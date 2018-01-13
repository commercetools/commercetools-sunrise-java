package com.commercetools.sunrise.controllers.version;

import com.commercetools.sunrise.controllers.VersionController;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class VersionControllerTest extends WithApplication {

    @Test
    public void showsVersion() throws Exception {
        final Result result = new VersionControllerTest(app).show();
        assertThat(result.status()).isEqualTo(Http.Status.OK);
        assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("version").contains("build");
    }

    private static class VersionControllerTest extends VersionController {

        VersionControllerTest(final Application application) {
            super(application);
        }
    }
}