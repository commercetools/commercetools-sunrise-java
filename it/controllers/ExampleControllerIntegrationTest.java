package controllers;

import com.commercetools.sunrise.common.WithSphereClient;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.junit.Test;

import java.util.List;

import static java.util.Locale.ENGLISH;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ExampleControllerIntegrationTest extends WithSphereClient {

    @Test
    public void itFindsSomeCategories() throws Exception {
        final PagedQueryResult<Category> result = execute(CategoryQuery.of());
        assertThat(result.getCount()).isGreaterThan(3);
        //this is a project specific assertion as example
        assertThat(toEnglishNames(result)).contains("Tank tops");
    }

    private List<String> toEnglishNames(final PagedQueryResult<Category> queryResult) {
        return queryResult.getResults().stream()
                .map(c -> c.getName().get(ENGLISH))
                .collect(toList());
    }
}
