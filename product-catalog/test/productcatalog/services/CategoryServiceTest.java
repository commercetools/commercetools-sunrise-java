package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import org.junit.Test;

import java.util.List;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceTest {

    private final CategoryTree categories = CategoryTree.of(readObjectFromResource("categories.json", CategoryQuery.resultTypeReference()).getResults());
    private final ProductProjection product = readObjectFromResource("productProjectionQuery.json", ProductProjectionQuery.resultTypeReference()).head().get();
    private final CategoryService categrieService = new CategoryService(categories);

    @Test
    public void getBreadCrumbCategories() {

        final Category woman = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
        final Category bags = categories.findById("32952779-d916-4f2b-b1d5-9efd7f7b9f58").get();

        assertThat(product.getCategories()).contains(bags.toReference());
        assertThat(bags.getParent()).contains(woman.toReference());
        assertThat(woman.getParent()).isEmpty();

        final List<Category> expectedResult = asList(woman, bags);
        final List<Category> result = categrieService.getBreadCrumbCategories(product);

        assertThat(result).isEqualTo(expectedResult);
    }
}
