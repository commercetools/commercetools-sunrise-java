package productcatalog.pages;

import common.pages.LinkData;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

public class ProductDetailPageContentTest {

    private final Translator translator = Translator.of(Locale.ENGLISH, emptyList(), emptyList());

    private final ProductProjection product =
            readObjectFromResource("productProjectionQuery.json", ProductProjectionQuery.resultTypeReference())
                    .head().get();

    private final CategoryTree categories = CategoryTree.of(readObjectFromResource("categories.json", CategoryQuery.resultTypeReference()).getResults());


    @Test
    public void additionalTitle() throws Exception {
        final ProductDetailPageContent content = new ProductDetailPageContent(null, translator, null, null, product, null, null, null, null);

        assertThat(content.additionalTitle()).isEqualTo("Bag ”Sutton” large Michael Kors");
    }

    @Test
    public void getBreadcrump() throws Exception {
        final Category women = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
        final Category clothing = categories.findById("5293b652-ff8a-46ed-ac3c-76e3e6887327").get();
        final Category blazer = categories.findById("d364a95e-6aea-4c00-903f-1ca914936741").get();

        final List<Category> breadcrumpCategories = asList(women, clothing, blazer);
        final List<LinkData> expectedResult = asList(new LinkData("WOMEN", ""), new LinkData("clothing", ""), new LinkData("blusen", ""));

        final ProductDetailPageContent content = new ProductDetailPageContent(null, translator, null, null, null, null, null, null, breadcrumpCategories);

        assertThat(content.getBreadcrumb()).isEqualTo(expectedResult);
    }
}