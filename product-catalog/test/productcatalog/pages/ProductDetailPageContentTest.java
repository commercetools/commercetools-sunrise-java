package productcatalog.pages;

import com.fasterxml.jackson.databind.JsonNode;
import com.neovisionaries.i18n.CountryCode;
import common.categories.CategoryUtils;
import common.cms.CmsPage;
import common.pages.*;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Image;
import io.sphere.sdk.models.ImageDimensions;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.shippingmethods.ShippingRate;
import org.javamoney.moneta.Money;
import org.junit.Test;
import productcatalog.models.RichShippingRate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static common.JsonUtils.readJsonNodeFromResource;
import static common.products.ProductUtils.getProductById;
import static common.products.ProductUtils.getQueryResult;
import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;
import static io.sphere.sdk.json.JsonUtils.toJsonNode;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDetailPageContentTest {
    private final CurrencyUnit eur = Monetary.getCurrency("EUR");
    private final CountryCode de = CountryCode.DE;
    private final Locale german = Locale.GERMAN;
    private final ZonedDateTime todayDe = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Berlin"));

    private final Translator translator = Translator.of(german, emptyList(), emptyList());
    private final PriceFinder priceFinder = PriceFinder.of(eur, de, Optional.empty(), Optional.empty(), todayDe);
    private final PriceFormatter priceFormatter = PriceFormatter.of(german);

    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categoryQueryResult.json").getResults());
    private final List<ProductProjection> products = getQueryResult("productProjectionQueryResult.json").getResults();

    @Test
    public void testStaticJson() throws IOException {
        final CmsPage cms = (messageKey, args) -> Optional.of(messageKey);
        final PdpStaticData pdpStaticData = new PdpStaticData(cms);

        final JsonNode expected = readJsonNodeFromResource("pdpStaticData.json");
        final JsonNode result = toJsonNode(pdpStaticData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testBreadcrumbJson() throws IOException {
        final Category woman = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
        final Category bags = categories.findById("32952779-d916-4f2b-b1d5-9efd7f7b9f58").get();
        final Category handBags = categories.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();
        final List<Category> breadcrumbs = asList(woman, bags, handBags);
        final CategoryLinkDataFactory categoryLinkDataFactory = CategoryLinkDataFactory.of(translator);
        final List<LinkData> breadcrumbData = breadcrumbs.stream().map(categoryLinkDataFactory::create).collect(toList());

        final JsonNode expected = readJsonNodeFromResource("breadcrumbData.json").get("breadcrumbs");
        final JsonNode result = toJsonNode(breadcrumbData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testGalleryJson() throws IOException {
        final ImageData firstImage = ImageData.of(Image.of("firstImage", ImageDimensions.of(100, 200)));
        final ImageData secondImage = ImageData.of(Image.of("secondImage", ImageDimensions.of(200, 300)));
        final ImageData thirdImage = ImageData.of(Image.of("thirdImage", ImageDimensions.of(300, 400)));
        final List<ImageData> galleryData = asList(firstImage, secondImage, thirdImage);

        final JsonNode expected = readJsonNodeFromResource("galleryData.json").get("gallery");
        final JsonNode result = toJsonNode(galleryData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testProductJson() throws IOException {
        final ProductProjection product = readObjectFromResource("product.json", ProductProjection.typeReference());
        final ProductVariant variant = product.getMasterVariant();
        final ProductData productData = ProductDataFactory.of(translator, priceFinder, priceFormatter).create(product, variant);

        final JsonNode expected = readJsonNodeFromResource("productData.json");
        final JsonNode result = toJsonNode(productData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testDeliveryJson() throws IOException {
        final RichShippingRate dhl = new RichShippingRate("DHL", ShippingRate.of(Money.of(10, "EUR")));
        final RichShippingRate dhlFreeAbove = new RichShippingRate("DHL", ShippingRate.of(Money.of(10, "EUR"), Money.of(50, "EUR")));
        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(priceFormatter);
        final List<ShippingRateData> deliveryData = asList(dhl, dhlFreeAbove).stream().map(shippingRateDataFactory::create).collect(toList());

        final JsonNode expected = readJsonNodeFromResource("deliveryData.json").get("deliveries");
        final JsonNode result = toJsonNode(deliveryData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSuggestionJson() throws IOException {
        final ProductProjection selma = getProductById(products, "4f643a44-5bed-415e-ae60-64c46bfb26f5");
        final ProductProjection dkny = getProductById(products, "a3f4588e-fcfe-41de-bd09-a071d76d697d");
        final ProductProjection miabag = getProductById(products, "dc9a4460-491c-48b4-bcf6-1d802bb7e164");
        final ProductProjection altea = getProductById(products, "4f643a44-5bed-415e-ae60-64c46bfb26f5");
        final ProductThumbnailDataFactory thumbnailDataBuilder = ProductThumbnailDataFactory.of(translator, priceFinder, priceFormatter);
        final List<ProductThumbnailData> suggestionData = asList(selma, dkny, miabag, altea).stream()
                .map(thumbnailDataBuilder::create).collect(toList());

        final JsonNode expected = readJsonNodeFromResource("suggestionData.json").get("suggestions");
        final JsonNode result = toJsonNode(suggestionData);

        assertThat(result).isEqualTo(expected);
    }
}