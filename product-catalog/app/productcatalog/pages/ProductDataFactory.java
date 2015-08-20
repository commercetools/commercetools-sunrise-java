package productcatalog.pages;

import common.pages.DetailData;
import common.pages.SelectableData;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.models.LocalizedEnumValue;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.productdiscounts.DiscountedPrice;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

public class ProductDataFactory {

    private static final AttributeAccess<String> TEXT_ATTR_ACCESS = AttributeAccess.ofText();
    private static final AttributeAccess<LocalizedEnumValue> LENUM_ATTR_ACCESS = AttributeAccess.ofLocalizedEnumValue();
    private static final AttributeAccess<Set<LocalizedString>> LENUM_SET_ATTR_ACCESS = AttributeAccess.ofLocalizedStringSet();

    private final Translator translator;
    private final PriceFinder priceFinder;
    private final PriceFormatter priceFormatter;

    private ProductDataFactory(final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.translator = translator;
        this.priceFinder = priceFinder;
        this.priceFormatter = priceFormatter;
    }

    public static ProductDataFactory of(final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductDataFactory(translator, priceFinder, priceFormatter);
    }

    public ProductData create(final ProductProjection product, final ProductVariant variant) {
        final Optional<Price> priceOpt = priceFinder.findPrice(variant.getPrices());

        return new ProductData(
                translator.findTranslation(product.getName()),
                Optional.ofNullable(variant.getSku()).orElse(""),
                Optional.ofNullable(product.getDescription()).map(translator::findTranslation).orElse(""),
                getPriceCurrent(priceOpt).map(price -> priceFormatter.format(price.getValue())).orElse(""),
                getPriceOld(priceOpt).map(price -> priceFormatter.format(price.getValue())).orElse(""),
                getColors(product),
                getSizes(product),
                getProductDetails(variant)
        );
    }

    private List<SelectableData> getColors(final ProductProjection product) {
        return getColorInAllVariants(product).stream()
                .map(this::colorToSelectableItem)
                .collect(toList());
    }

    private List<SelectableData> getSizes(final ProductProjection product) {
        return getSizeInAllVariants(product).stream()
                .map(this::sizeToSelectableItem)
                .collect(toList());
    }

    private List<DetailData> getProductDetails(final ProductVariant variant) {
        return variant.findAttribute("details", LENUM_SET_ATTR_ACCESS).orElse(emptySet()).stream()
                .map(this::localizedStringsToDetailData)
                .collect(toList());
    }

    private List<Attribute> getColorInAllVariants(final ProductProjection product) {
        return getAttributeInAllVariants(product, "color");
    }

    private List<Attribute> getSizeInAllVariants(final ProductProjection product) {
        return getAttributeInAllVariants(product, "size");
    }

    private List<Attribute> getAttributeInAllVariants(final ProductProjection product, final String attributeName) {
        return product.getAllVariants().stream()
                .map(variant -> Optional.ofNullable(variant.getAttribute(attributeName)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(toList());
    }

    private SelectableData colorToSelectableItem(final Attribute color) {
        final String colorLabel = translator.findTranslation(color.getValue(LENUM_ATTR_ACCESS).getLabel());
        return new SelectableData(colorLabel, color.getName(), "", "", false);
    }

    private SelectableData sizeToSelectableItem(final Attribute size) {
        final String sizeLabel = size.getValue(TEXT_ATTR_ACCESS);
        return new SelectableData(sizeLabel, sizeLabel, "", "", false);
    }

    private DetailData localizedStringsToDetailData(final LocalizedString localizedStrings) {
        final String label = translator.findTranslation(localizedStrings);
        return new DetailData(label, "");
    }

    private Optional<Price> getPriceOld(final Optional<Price> priceOpt) {
        return priceOpt.flatMap(price -> Optional.ofNullable(price.getDiscounted()).map(discountedPrice -> price));
    }

    private Optional<Price> getPriceCurrent(final Optional<Price> priceOpt) {
        return priceOpt.map(price -> Optional.ofNullable(price.getDiscounted()).map(DiscountedPrice::getValue)
                .orElse(price.getValue()))
                .map(Price::of);
    }
}
