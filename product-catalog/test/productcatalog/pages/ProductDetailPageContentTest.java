package productcatalog.pages;

import common.pages.ImageData;
import common.pages.LinkData;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Image;
import io.sphere.sdk.models.ImageDimensions;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;
import productcatalog.common.CategoryTestData;
import productcatalog.common.ProductTestData;

import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDetailPageContentTest {

    private final Translator translator = Translator.of(Locale.ENGLISH, emptyList(), emptyList());

    private final List<ProductProjection> products = ProductTestData.of().getProducts();
    private final CategoryTree categories = CategoryTree.of(CategoryTestData.of().getCategories());

    @Test
    public void additionalTitle() throws Exception {
        final ProductProjection suttonBag = products.get(6);
        final ProductDetailPageContent content = new ProductDetailPageContent(null, translator, null, null, suttonBag, null, null, null, null);

        assertThat(suttonBag.getId()).isEqualTo("254f1c0e-67bc-4aa6-992d-9a0fea1846b5");
        assertThat(content.additionalTitle()).isEqualTo("Bag ”Sutton” large Michael Kors");
    }

    @Test
    public void getBreadcrump() throws Exception {
        final Category women = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
        final Category clothing = categories.findById("5293b652-ff8a-46ed-ac3c-76e3e6887327").get();
        final Category blazer = categories.findById("d364a95e-6aea-4c00-903f-1ca914936741").get();
        final List<Category> breadcrumpCategories = asList(women, clothing, blazer);

        final ProductDetailPageContent content = new ProductDetailPageContent(null, translator, null, null, null, null, null, null, breadcrumpCategories);
        final List<LinkData> expectedResult = asList(new LinkData("WOMEN", ""), new LinkData("clothing", ""), new LinkData("blusen", ""));

        assertThat(content.getBreadcrumb()).isEqualTo(expectedResult);
    }

    @Test
    public void getGallery() throws Exception {
        final ProductProjection maximilian16 = products.get(0);
        final Image image1 = Image.of("https://s3-eu-west-1.amazonaws.com/commercetools-maximilian/products/s15lookd16_1_medium.jpg", ImageDimensions.of(0, 0));
        final Image image2 = Image.of("https://s3-eu-west-1.amazonaws.com/commercetools-maximilian/products/s15lookd16_2_medium.jpg", ImageDimensions.of(0, 0));
        final Image image3 = Image.of("https://s3-eu-west-1.amazonaws.com/commercetools-maximilian/products/s15lookd16_3_medium.jpg", ImageDimensions.of(0, 0));

        final ProductDetailPageContent content = new ProductDetailPageContent(null, null, null, null, null, maximilian16.getMasterVariant(), null, null, null);
        final List<ImageData> expectedResult = asList(ImageData.of(image1), ImageData.of(image2), ImageData.of(image3));

        assertThat(maximilian16.getId()).isEqualTo("2a439c1c-1687-4499-9d5a-8d5e3e6620a4");
        assertThat(content.getGallery()).isEqualTo(expectedResult);
    }
}