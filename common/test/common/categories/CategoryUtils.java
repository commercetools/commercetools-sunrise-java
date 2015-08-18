package common.categories;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;

public class CategoryUtils {

    public static PagedQueryResult<Category> getQueryResult(final String resourceName) {
        return readObjectFromResource(resourceName, CategoryQuery.resultTypeReference());
    }


}
