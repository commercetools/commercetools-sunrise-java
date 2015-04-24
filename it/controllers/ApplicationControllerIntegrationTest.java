package controllers;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.junit.Test;
import testutils.WithPlayJavaSphereClient;

import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;

public class ApplicationControllerIntegrationTest extends WithPlayJavaSphereClient {
    @Test
    public void itFindsSomeCategories() throws Exception {
        final PagedQueryResult<Category> queryResult = client.execute(CategoryQuery.of()).get(4000);
        final int count = queryResult.size();
        assertThat(count).isGreaterThan(3);
        //this is a project specific assertion as example
        assertThat(queryResult.getResults().get(0).getName().get(Locale.ENGLISH).get()).isEqualTo("Tank tops");
    }
}
