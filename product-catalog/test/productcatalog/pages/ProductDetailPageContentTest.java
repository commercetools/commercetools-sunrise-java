package productcatalog.pages;

import com.fasterxml.jackson.databind.JsonNode;
import com.neovisionaries.i18n.CountryCode;
import common.categories.CategoryUtils;
import common.cms.CmsPage;
import common.contexts.UserContext;
import common.pages.*;
import common.utils.PriceFormatter;
import common.utils.PriceFormatterImpl;
import common.utils.TranslationResolverImpl;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ImageDimensions;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.shippingmethods.ShippingRate;
import org.javamoney.moneta.Money;
import org.junit.Test;
import productcatalog.models.ShopShippingRate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static common.JsonUtils.readJsonNodeFromResource;
import static common.products.ProductUtils.getProductById;
import static common.products.ProductUtils.getQueryResult;
import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static io.sphere.sdk.json.SphereJsonUtils.toJsonNode;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDetailPageContentTest {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");
    private final CurrencyUnit eur = Monetary.getCurrency("EUR");
    private final CountryCode de = CountryCode.DE;
    private final Locale german = Locale.GERMAN;

    private final TranslationResolverImpl translator = TranslationResolverImpl.of(german, emptyList(), emptyList());
    private final PriceFormatter priceFormatter = PriceFormatterImpl.of(german);
    private final UserContext userContext = UserContext.of(de, german, emptyList(), ZONE_ID, eur, null, null);

    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categoryQueryResult.json").getResults());
    private final List<ProductProjection> products = getQueryResult("productProjectionQueryResult.json").getResults();

    @Test
    public void staticJson() throws IOException {
        final CmsPage cms = (messageKey, args) -> Optional.of(messageKey);
        final PdpStaticData pdpStaticData = new PdpStaticData(cms, BagItemDataFactory.of().create(100), RatingDataFactory.of(cms).create());

        final JsonNode expected = readJsonNodeFromResource("pdpStaticData.json");
        final JsonNode result = toJsonNode(pdpStaticData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void breadcrumbJson() throws IOException {
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
    public void galleryJson() throws IOException {
        final ImageData firstImage = ImageData.of(Image.of("firstImage", ImageDimensions.of(100, 200)));
        final ImageData secondImage = ImageData.of(Image.of("secondImage", ImageDimensions.of(200, 300)));
        final ImageData thirdImage = ImageData.of(Image.of("thirdImage", ImageDimensions.of(300, 400)));
        final List<ImageData> galleryData = asList(firstImage, secondImage, thirdImage);

        final JsonNode expected = readJsonNodeFromResource("galleryData.json").get("gallery");
        final JsonNode result = toJsonNode(galleryData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void productJson() throws IOException {
        final ProductProjection product = readObjectFromResource("product.json", ProductProjection.typeReference());
        final ProductVariant variant = product.getMasterVariant();
        final ProductData productData = ProductDataFactory.of(userContext).create(product, variant);

        final JsonNode expected = readJsonNodeFromResource("productData.json");
        final JsonNode result = toJsonNode(productData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deliveryJson() throws IOException {
        final ShopShippingRate dhl = new ShopShippingRate("DHL", ShippingRate.of(Money.of(10, "EUR")));
        final ShopShippingRate dhlFreeAbove = new ShopShippingRate("DHL", ShippingRate.of(Money.of(10, "EUR"), Money.of(50, "EUR")));
        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(priceFormatter);
        final List<ShippingRateData> deliveryData = asList(dhl, dhlFreeAbove).stream().map(shippingRateDataFactory::create).collect(toList());

        final JsonNode expected = readJsonNodeFromResource("deliveryData.json").get("deliveries");
        final JsonNode result = toJsonNode(deliveryData);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void suggestionJson() throws IOException {
        final ProductProjection selma = getProductById(products, "4f643a44-5bed-415e-ae60-64c46bfb26f5");
        final ProductProjection dkny = getProductById(products, "a3f4588e-fcfe-41de-bd09-a071d76d697d");
        final ProductProjection miabag = getProductById(products, "dc9a4460-491c-48b4-bcf6-1d802bb7e164");
        final ProductProjection altea = getProductById(products, "4f643a44-5bed-415e-ae60-64c46bfb26f5");
        final ProductThumbnailDataFactory thumbnailDataBuilder = ProductThumbnailDataFactory.of(userContext);
        final List<ProductThumbnailData> suggestionData = asList(selma, dkny, miabag, altea).stream()
                .map(thumbnailDataBuilder::create).collect(toList());

        final JsonNode expected = readJsonNodeFromResource("suggestionData.json").get("suggestions");
        final JsonNode result = toJsonNode(suggestionData);

        assertThat(result).isEqualTo(expected);
    }
}