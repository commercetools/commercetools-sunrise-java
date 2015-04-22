package controllers;

import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.queries.PagedQueryResult;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.function.Function;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class ApplicationControllerTest extends WithSunriseApplication {

    @Test
    public void indexDisplaysCategories() {
        checkCategoryCorrectlyDisplayed("en-US,en");//using existing locale
    }

    @Test
    public void englishIsFallbackLanguage() {
        checkCategoryCorrectlyDisplayed("fr-FR");//using not existing locale
    }

    private void checkCategoryCorrectlyDisplayed(final String lang) {
        final Result result = route(new Http.RequestBuilder().header("Accept-Language", lang).path(generalpages.controllers.routes.HomeController.index().url()));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).contains("TestSnowboard equipment");
    }

    @Override
    protected Function<HttpRequestIntent, Object> getTestDoubleBehavior() {
        return intent -> PagedQueryResult.empty();
    }
}
