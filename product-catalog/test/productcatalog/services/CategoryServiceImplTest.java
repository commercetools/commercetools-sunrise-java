package productcatalog.services;

import common.categories.CategoryUtils;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static common.products.ProductUtils.getProductById;
import static common.products.ProductUtils.getQueryResult;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceImplTest {

    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categoryQueryResult.json").getResults());
    private final List<ProductProjection> products = getQueryResult("productProjectionQueryResult.json").getResults();
    private final CategoryService categrieService = new CategoryServiceImpl(categories);

    @Test
    @SuppressWarnings("unchecked")
    public void getSiblingCategories() throws Exception {
        final Category shoulderBags = categories.findById("30d79426-a17a-4e63-867e-ec31a1a33416").get();
        final Category handBags = categories.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();
        final Category clutches = categories.findById("a9c9ebd8-e6ff-41a6-be8e-baa07888c9bd").get();
        final Category satchels = categories.findById("30d79426-a17a-4e63-867e-ec31a1a33416").get();
        final Category shoppers = categories.findById("bd83e288-77de-4c3a-a26c-8384af715bbb").get();
        final Category wallets = categories.findById("d2f9a2da-db3e-4ee8-8192-134ebbc7fe4a").get();
        final Category backpacks = categories.findById("46249239-8f0f-48a9-b0a0-d29b37fc617f").get();
        final Category slingBags = categories.findById("8e052705-7810-4528-ba77-00094b87a69a").get();

        final List<Category> shoulderBagSiblings = categrieService.getSiblings(singletonList(shoulderBags.toReference()));
        final List<Category> handBagSiblings = categrieService.getSiblings(singletonList(handBags.toReference()));
        final List<Category> combinedsiblings = categrieService.getSiblings(asList(shoulderBags.toReference(), handBags.toReference()));

        assertThat(shoulderBagSiblings)
                .containsExactly(clutches, satchels, shoppers, handBags, wallets, backpacks, slingBags);
        assertThat(handBagSiblings)
                .containsExactly(clutches, satchels, shoppers, handBags, wallets, backpacks, slingBags);
        assertThat(combinedsiblings)
                .containsExactly(clutches, satchels, shoppers, handBags, wallets, backpacks, slingBags);
    }


}
