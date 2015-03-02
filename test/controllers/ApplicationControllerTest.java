package controllers;

import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.Query;
import io.sphere.sdk.queries.QueryDsl;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;

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

    private void checkCategoryCorrectlyDisplayed(String lang) {
        Result result = callAction(
                controllers.routes.ref.ApplicationController.index(),
                new FakeRequest(GET, "/").withHeader("Accept-Language", lang)
        );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentAsString(result)).contains("TestSnowboard equipment");
    }

    @Override
    protected Function<HttpRequestIntent, Object> getTestDoubleBehavior() {
        return requestable -> {
            return PagedQueryResult.empty();
        };
    }

    private <I> boolean isCategoryEndpoint(final SphereRequest<I> query) {
        return (query instanceof QueryDsl) && ((QueryDsl) query).endpoint().equals("/categories");
    }
}
