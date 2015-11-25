package productcatalog.services;

import common.categories.JsonUtils;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.models.Reference;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceImplTest {

    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(JsonUtils.readJson("categoryQueryResult.json", CategoryQuery.resultTypeReference()).getResults());
    private static final CategoryService CATEGORY_SERVICE = new CategoryServiceImpl(CATEGORY_TREE);
    private static final Category handBags = CATEGORY_TREE.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();
    private static final Category clutches = CATEGORY_TREE.findById("a9c9ebd8-e6ff-41a6-be8e-baa07888c9bd").get();
    private static final Category satchels = CATEGORY_TREE.findById("30d79426-a17a-4e63-867e-ec31a1a33416").get();
    private static final Category shoppers = CATEGORY_TREE.findById("bd83e288-77de-4c3a-a26c-8384af715bbb").get();
    private static final Category wallets = CATEGORY_TREE.findById("d2f9a2da-db3e-4ee8-8192-134ebbc7fe4a").get();
    private static final Category backpacks = CATEGORY_TREE.findById("46249239-8f0f-48a9-b0a0-d29b37fc617f").get();
    private static final Category slingBags = CATEGORY_TREE.findById("8e052705-7810-4528-ba77-00094b87a69a").get();

    @Test
    public void siblingsOfShoulderBags() throws Exception {
        test(singletonList(satchels.toReference()),
                siblings -> assertThat(siblings).containsExactly(clutches, shoppers, handBags, wallets, backpacks, slingBags));
    }

    @Test
    public void siblingsOfHandBags() throws Exception {
        test(singletonList(handBags.toReference()),
                siblings -> assertThat(siblings).containsExactly(clutches, satchels, shoppers, wallets, backpacks, slingBags));
    }

    @Test
    public void siblingsOfCombined() throws Exception {
        test(asList(satchels.toReference(), handBags.toReference()),
                siblings -> assertThat(siblings).containsExactly(clutches, shoppers, wallets, backpacks, slingBags));
    }

    private void test(final List<Reference<Category>> categories, final Consumer<List<Category>> test) {
        final List<Category> siblings = CATEGORY_SERVICE.getSiblings(categories);
        test.accept(siblings);
    }
}
