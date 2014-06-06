package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import fixtures.Fixtures;
import io.sphere.sdk.categories.CategoryImpl;
import io.sphere.sdk.client.PagedQueryResult;
import io.sphere.sdk.client.Query;
import io.sphere.sdk.client.SphereRequestExecutor;
import io.sphere.sdk.client.SphereRequestExecutorTestDouble;
import io.sphere.sdk.queries.EntityQueryWithCopyImpl;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;

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
    protected SphereRequestExecutor getSphereRequestExecutor() {
        return new SphereRequestExecutorTestDouble() {
            @Override
            protected <I, R> PagedQueryResult<I> result(Query<I, R> query) {
                final PagedQueryResult<I> result;
                if (isCategoryEndpoint(query)) {
                    final TypeReference<PagedQueryResult<CategoryImpl>> typeReference = new TypeReference<PagedQueryResult<CategoryImpl>>() {

                    };
                    result = Fixtures.readJsonFromClasspath("categories.json", typeReference);
                } else {
                    result = super.result(query);
                }
                return result;
            }
        };
    }

    private <I, R> boolean isCategoryEndpoint(final Query<I, R> query) {
        return (query instanceof EntityQueryWithCopyImpl) && ((EntityQueryWithCopyImpl) query).endpoint().equals("/categories");
    }
}
