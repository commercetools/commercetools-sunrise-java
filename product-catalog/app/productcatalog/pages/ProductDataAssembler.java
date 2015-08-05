package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.CollectionData;
import common.pages.DetailData;
import common.pages.SelectableData;
import common.pages.ShippingData;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.attributes.Attribute;
import io.sphere.sdk.attributes.AttributeAccess;
import io.sphere.sdk.models.LocalizedEnumValue;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.ShippingRate;
import io.sphere.sdk.zones.Zone;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class ProductDataAssembler {

    private static final AttributeAccess<String> TEXT_ATTR_ACCESS = AttributeAccess.ofText();
    private static final AttributeAccess<LocalizedEnumValue> LENUM_ATTR_ACCESS = AttributeAccess.ofLocalizedEnumValue();
    private static final AttributeAccess<Set<LocalizedStrings>> LENUM_SET_ATTR_ACCESS = AttributeAccess.ofLocalizedStringsSet();

    private final CmsPage cms;
    private final Translator translator;
    private final PriceFinder priceFinder;
    private final PriceFormatter priceFormatter;

    private ProductDataAssembler(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.cms = cms;
        this.translator = translator;
        this.priceFinder = priceFinder;
        this.priceFormatter = priceFormatter;
    }

    public static ProductDataAssembler of(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductDataAssembler(cms, translator, priceFinder, priceFormatter);
    }

    public ProductData assembleProduct(final ProductProjection product, final ProductVariant variant, final List<ShippingMethod> shippingMethods) {
        final Optional<Price> priceOpt = priceFinder.findPrice(variant.getPrices());
        final Reference<Zone> zone = Reference.of(Zone.typeId(), "f77ddfd4-af5b-471a-89c5-9a40d8a7ab88");

        return ProductDataBuilder.of()
                .withText(translator.translate(product.getName()))
                .withSku(variant.getSku().orElse(""))
                .withRatingList(assembleRatingList())
                .withDescription(product.getDescription().map(translator::translate).orElse(""))
                .withViewDetailsText(cms.getOrEmpty("product.viewDetails"))
                .withPrice(priceOpt.map(price -> priceFormatter.format(price.getValue())).orElse(""))
                .withPriceOld(getPriceOld(priceOpt).map(price -> priceFormatter.format(price.getValue())).orElse(""))
                .withColorList(assembleColorList(product))
                .withSizeList(assembleSizeList(product))
                .withSizeGuideText(cms.getOrEmpty("product.sizeGuide"))
                .withBagItemList(assembleBagItemList())
                .withAddToBagText(cms.getOrEmpty("product.addToBag"))
                .withAddToWishlistText(cms.getOrEmpty("product.addToWishlist"))
                .withAvailableText(cms.getOrEmpty("product.available"))
                .withProductDetails(assembleProductDetails(variant))
                .withDeliveryAndReturns(cms.getOrEmpty("product.deliveryAndReturn.text"))
                .withDelivery(assembleDeliveryList(shippingMethods, zone))
                .withReturns(assembleReturnsList(shippingMethods))
                .build();
    }

    private CollectionData<SelectableData> assembleRatingList() {
        return new CollectionData<>("", asList(
                new SelectableData("5 Stars", "5", cms.getOrEmpty("product.ratingList.five.text"), "", false),
                new SelectableData("4 Stars", "4", cms.getOrEmpty("product.ratingList.four.text"), "", false),
                new SelectableData("3 Stars", "3", cms.getOrEmpty("product.ratingList.three.text"), "", false),
                new SelectableData("2 Stars", "2", cms.getOrEmpty("product.ratingList.two.text"), "", false),
                new SelectableData("1 Stars", "1", cms.getOrEmpty("product.ratingList.one.text"), "", false)
        ));
    }

    private CollectionData<SelectableData> assembleColorList(final ProductProjection product) {
        final SelectableData defaultItem = new SelectableData(cms.getOrEmpty("product.colorList.choose.text"), "none", "", "", true);
        final Stream<SelectableData> defaultItemStream = Stream.of(defaultItem);
        final Stream<SelectableData> selectableDataStream = getColorInAllVariants(product).stream()
                .map(this::colorToSelectableItem);

        final List<SelectableData> colors = concat(defaultItemStream, selectableDataStream).collect(toList());
        return new CollectionData<>(cms.getOrEmpty("product.colorList.text"), colors);
    }

    private CollectionData<SelectableData> assembleSizeList(final ProductProjection product) {
        final SelectableData defaultItem = new SelectableData(cms.getOrEmpty("product.sizeList.choose.text"), "none", "", "", true);
        final Stream<SelectableData> defaultItemStream = Stream.of(defaultItem);
        final Stream<SelectableData> selectableDataStream = getSizeInAllVariants(product).stream()
                .map(this::sizeToSelectableItem);

        final List<SelectableData> sizes = concat(defaultItemStream, selectableDataStream).collect(toList());
        return new CollectionData<>(cms.getOrEmpty("product.sizeList.text"), sizes);
    }

    private CollectionData<SelectableData> assembleBagItemList() {
        final SelectableData defaultItem = new SelectableData("1", "", "", "", true);
        final Stream<SelectableData> defaultItemStream = Stream.of(defaultItem);
        final Stream<SelectableData> selectableDataStream = IntStream.range(2, 100)
                .mapToObj(this::bagItemToSelectableItem);

        final List<SelectableData> bagItems = concat(defaultItemStream, selectableDataStream).collect(toList());
        return new CollectionData<>("", bagItems);
    }

    private CollectionData<DetailData> assembleProductDetails(final ProductVariant variant) {
        final List<DetailData> details = variant.getAttribute("details", LENUM_SET_ATTR_ACCESS).orElse(emptySet()).stream()
                .map(this::localizedStringsToDetailData)
                .collect(toList());

        return new CollectionData<>(cms.getOrEmpty("product.productDetails.text"), details);
    }

    private CollectionData<ShippingData> assembleDeliveryList(final List<ShippingMethod> shippingMethods, final Reference<Zone> zone) {
        final List<ShippingData> deliveryInformation = shippingMethods.stream()
                .flatMap(shippingMethod -> getDeliveryInformation(shippingMethod, zone).stream())
                .collect(toList());

        return new CollectionData<>(cms.getOrEmpty("product.delivery.text"), deliveryInformation);
    }

    private List<ShippingData> getDeliveryInformation(final ShippingMethod shippingMethod, final Reference<Zone> zone) {
        return shippingMethod.getShippingRatesForZone(zone).stream()
                .map(shippingRate -> shippingMethodToShippingData(shippingMethod, shippingRate))
                .collect(toList());
    }

    private CollectionData<ShippingData> assembleReturnsList(final List<ShippingMethod> shippingMethods) {
        return new CollectionData<>(cms.getOrEmpty("product.returns.text"), emptyList());
    }

    private Optional<Price> getPriceOld(final Optional<Price> priceOpt) {
        return priceOpt.flatMap(price -> price.getDiscounted().map(discountedPrice -> price));
    }

    private List<Attribute> getColorInAllVariants(final ProductProjection product) {
        return getAttributeInAllVariants(product, "color");
    }

    private List<Attribute> getSizeInAllVariants(final ProductProjection product) {
        return getAttributeInAllVariants(product, "size");
    }

    private List<Attribute> getAttributeInAllVariants(final ProductProjection product, final String attributeName) {
        return product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attributeName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(toList());
    }

    private SelectableData colorToSelectableItem(final Attribute color) {
        final String colorLabel = translator.translate(color.getValue(LENUM_ATTR_ACCESS).getLabel());
        return new SelectableData(colorLabel, color.getName(), "", "", false);
    }

    private SelectableData sizeToSelectableItem(final Attribute size) {
        final String sizeLabel = size.getValue(TEXT_ATTR_ACCESS);
        return new SelectableData(sizeLabel, sizeLabel, "", "", false);
    }

    private SelectableData bagItemToSelectableItem(final int number) {
        final String bagItemLabel = Integer.toString(number);
        return new SelectableData(bagItemLabel, "", "", "", false);
    }

    private DetailData localizedStringsToDetailData(final LocalizedStrings localizedStrings) {
        final String label = translator.translate(localizedStrings);
        return new DetailData(label, "");
    }

    private ShippingData shippingMethodToShippingData(final ShippingMethod shippingMethod, final ShippingRate shippingRate) {
        return new ShippingData(
                shippingMethod.getName(),
                "",
                priceFormatter.format(shippingRate.getPrice()),
                shippingRate.getFreeAbove().map(amount -> "Free above" + priceFormatter.format(amount)).orElse("")
        );
    }
}
