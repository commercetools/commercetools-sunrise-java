package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;
import productcatalog.common.CategoryTestData;
import productcatalog.common.ProductTestData;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceTest {

    private final CategoryTree categories = CategoryTree.of(CategoryTestData.of().getCategories());
    private final List<ProductProjection> products = ProductTestData.of().getProducts();
    private final CategoryService categrieService = new CategoryService(categories);

    @Test
    public void getBreadCrumbCategories() {

        final ProductProjection suttonBag = products.get(6);

        final Category woman = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
        final Category bags = categories.findById("32952779-d916-4f2b-b1d5-9efd7f7b9f58").get();
        final Category shoulderBags = categories.findById("30d79426-a17a-4e63-867e-ec31a1a33416").get();

        final List<Category> expectedResult = asList(woman, bags, shoulderBags);
        final List<Category> result = categrieService.getBreadCrumbCategories(suttonBag);

        assertThat(suttonBag.getId()).isEqualTo("254f1c0e-67bc-4aa6-992d-9a0fea1846b5");
        assertThat(result).isEqualTo(expectedResult);
    }
}
