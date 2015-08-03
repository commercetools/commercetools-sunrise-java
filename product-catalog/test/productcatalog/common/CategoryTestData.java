package productcatalog.common;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.List;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;

public class CategoryTestData {

    private final PagedQueryResult<Category> queryResult =
            readObjectFromResource("categoryQueryResult.json", CategoryQuery.resultTypeReference());

    private CategoryTestData() {

    }

    public static CategoryTestData of() {
        return new CategoryTestData();
    }

    public List<Category> getCategories() {
        return queryResult.getResults();
    }
}
