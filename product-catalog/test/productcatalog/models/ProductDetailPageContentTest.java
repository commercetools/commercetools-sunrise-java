package productcatalog.models;

import common.categories.JsonUtils;
import common.contexts.UserContext;

import common.utils.PriceFormatter;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.ProductProjection;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

import static com.neovisionaries.i18n.CountryCode.DE;
import static common.categories.JsonUtils.readJson;
import static common.products.ProductUtils.getQueryResult;
import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Collections.singletonList;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDetailPageContentTest {
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");
    private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");
    private static final List<Locale> LOCALES = singletonList(GERMAN);
    private static final PriceFormatter PRICE_FORMATTER = PriceFormatter.of(GERMAN);
    private static final UserContext USER_CONTEXT = UserContext.of(DE, LOCALES, ZONE_ID, EUR, null, null);

    private final CategoryTree categories = CategoryTree.of(readJson("categoryQueryResult.json", CategoryQuery.resultTypeReference()).getResults());
    private final List<ProductProjection> products = getQueryResult("productProjectionQueryResult.json").getResults();

//    @Test
//    public void staticJson() throws IOException {
//        final CmsPage cms = (messageKey, args) -> Optional.of(messageKey);
//        final PdpStaticData pdpStaticData = new PdpStaticData(cms, BagItemDataFactory.of().create(100), RatingDataFactory.of(cms).create());
//
//        final JsonNode expected = readJsonNodeFromResource("pdpStaticData.json");
//        final JsonNode result = toJsonNode(pdpStaticData);
//
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    public void breadcrumbJson() throws IOException {
//        final Category woman = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
//        final Category bags = categories.findById("32952779-d916-4f2b-b1d5-9efd7f7b9f58").get();
//        final Category handBags = categories.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();
//        final List<Category> breadcrumbs = asList(woman, bags, handBags);
//        final CategoryLinkDataFactory categoryLinkDataFactory = CategoryLinkDataFactory.of(LOCALES);
//        final List<LinkData> breadcrumbData = breadcrumbs.stream().map(categoryLinkDataFactory::create).collect(toList());
//
//        final JsonNode expected = readJsonNodeFromResource("breadcrumbData.json").get("breadcrumbs");
//        final JsonNode result = toJsonNode(breadcrumbData);
//
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Ignore
//    @Test
//    public void productJson() throws IOException {
//        final ProductProjection product = readObjectFromResource("product.json", ProductProjection.typeReference());
//        final ProductVariant variant = product.getMasterVariant();
//        final ProductData productData = ProductDataFactory.of(USER_CONTEXT).create(product, variant);
//
//        final JsonNode expected = readJsonNodeFromResource("productData.json");
//        final JsonNode result = toJsonNode(productData);
//
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    public void deliveryJson() throws IOException {
//        final ShopShippingRate dhl = new ShopShippingRate("DHL", ShippingRate.of(Money.of(10, "EUR")));
//        final ShopShippingRate dhlFreeAbove = new ShopShippingRate("DHL", ShippingRate.of(Money.of(10, "EUR"), Money.of(50, "EUR")));
//        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(PRICE_FORMATTER);
//        final List<ShippingRateData> deliveryData = asList(dhl, dhlFreeAbove).stream().map(shippingRateDataFactory::create).collect(toList());
//
//        final JsonNode expected = readJsonNodeFromResource("deliveryData.json").get("deliveries");
//        final JsonNode result = toJsonNode(deliveryData);
//
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Ignore
//    @Test
//    public void suggestionJson() throws IOException {
//        final ProductProjection selma = getProductById(products, "4f643a44-5bed-415e-ae60-64c46bfb26f5");
//        final ProductProjection dkny = getProductById(products, "a3f4588e-fcfe-41de-bd09-a071d76d697d");
//        final ProductProjection miabag = getProductById(products, "dc9a4460-491c-48b4-bcf6-1d802bb7e164");
//        final ProductProjection altea = getProductById(products, "4f643a44-5bed-415e-ae60-64c46bfb26f5");
//        final ProductDataFactory productDataFactory = ProductDataFactory.of(USER_CONTEXT);
//        final List<ProductData> suggestionData = asList(selma, dkny, miabag, altea).stream()
//                .map(product -> productDataFactory.create(product, product.getMasterVariant())).collect(toList());
//
//        final JsonNode expected = readJsonNodeFromResource("suggestionData.json").get("suggestions");
//        final JsonNode result = toJsonNode(suggestionData);
//
//        assertThat(result).isEqualTo(expected);
//    }
}