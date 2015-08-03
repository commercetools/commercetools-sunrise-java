package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;
import productcatalog.common.CategoryTestData;
import productcatalog.common.ProductTestData;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceTest {

    private final CategoryTree categories = CategoryTree.of(CategoryTestData.of().getCategories());
    private final List<ProductProjection> products = ProductTestData.of().getProducts();
    private final CategoryService categrieService = new CategoryService(categories);

    @Test
    public void getSiblingCategories() {
        final ProductProjection suttonBag = products.get(6);
        final Category clutches = categories.findById("a9c9ebd8-e6ff-41a6-be8e-baa07888c9bd").get();
        final Category satchels = categories.findById("30d79426-a17a-4e63-867e-ec31a1a33416").get();
        final Category shoppers = categories.findById("bd83e288-77de-4c3a-a26c-8384af715bbb").get();
        final Category handBags = categories.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();
        final Category wallets = categories.findById("d2f9a2da-db3e-4ee8-8192-134ebbc7fe4a").get();
        final Category backpacks = categories.findById("46249239-8f0f-48a9-b0a0-d29b37fc617f").get();
        final Category shoulderBags = categories.findById("8e052705-7810-4528-ba77-00094b87a69a").get();

        final List<Category> expectedResult = asList(clutches, satchels, shoppers, handBags, wallets, backpacks, shoulderBags);
        final List<Category> result = categrieService.getSiblingCategories(suttonBag);

        assertThat(suttonBag.getId()).isEqualTo("254f1c0e-67bc-4aa6-992d-9a0fea1846b5");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void getBreadCrumbCategories() {
        final ProductProjection suttonBag = products.get(6);
        final Category woman = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
        final Category bags = categories.findById("32952779-d916-4f2b-b1d5-9efd7f7b9f58").get();
        final Category handBags = categories.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();

        final List<Category> expectedResult = asList(woman, bags, handBags);
        final List<Category> result = categrieService.getBreadCrumbCategories(suttonBag);

        assertThat(suttonBag.getId()).isEqualTo("254f1c0e-67bc-4aa6-992d-9a0fea1846b5");
        assertThat(result).isEqualTo(expectedResult);
    }
}
