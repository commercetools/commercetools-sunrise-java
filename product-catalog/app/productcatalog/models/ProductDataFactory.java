package productcatalog.models;

import common.contexts.PriceFinderFactory;
import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.models.LocalizedEnumValue;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.NamedAttributeAccess;

import java.util.Set;

public class ProductDataFactory {

    private static final NamedAttributeAccess<LocalizedEnumValue> COLOR_ATTRIBUTE = AttributeAccess.ofLocalizedEnumValue().ofName("color");
    private static final NamedAttributeAccess<String> SIZE_ATTRIBUTE = AttributeAccess.ofText().ofName("size");
    private static final NamedAttributeAccess<Set<LocalizedEnumValue>> DETAILS_ATTRIBUTE = AttributeAccess.ofLocalizedEnumValueSet().ofName("details");

    private final UserContext userContext;
    private final PriceFormatter priceFormatter;
    private final PriceFinder priceFinder;
    private final ReverseRouter reverseRouter;

    private ProductDataFactory(final UserContext userContext, final ReverseRouter reverseRouter) {
        this.userContext = userContext;
        this.priceFormatter = PriceFormatter.of(userContext.locale());
        this.priceFinder = PriceFinderFactory.create(userContext);
        this.reverseRouter = reverseRouter;
    }

//    public static ProductDataFactory of(final UserContext userContext, final ReverseRouter reverseRouter, final CategoryService categoryService) {
//        return new ProductDataFactory(userContext, reverseRouter, categoryService);
//    }
//
//    public ProductData create(final ProductProjection product, final ProductVariant variant) {
//        final boolean isSale = priceOld.isPresent();
//        final boolean isNew = productIsNew(product);
//        return new ProductData(name, sku, description, formatPriceOpt(price), formatPriceOpt(priceOld), getImages(variant), getColors(product), getSizes(product, variant, slug), getDetails(variant));
//    }
//
//    private List<SelectableData> getColors(final ProductProjection product) {
//        return getColorInAllVariants(product).stream()
//                .map(this::colorToSelectableItem)
//                .collect(toList());
//    }
//
//    private List<SelectableData> getSizes(final ProductProjection product, final ProductVariant currentVariant, final String slug) {
//        final String currentSize = currentVariant.findAttribute(SIZE_ATTRIBUTE).orElse("");
//        return product.getAllVariants().stream()
//                .map(variant -> sizeToSelectableItem(variant, slug, currentSize))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .distinct()
//                .collect(toList());
//    }
//
//    private List<DetailData> getDetails(final ProductVariant variant) {
//        return variant.findAttribute(DETAILS_ATTRIBUTE).orElse(emptySet()).stream()
//                .map(this::localizedStringsToDetailData)
//                .collect(toList());
//    }
//
//    private List<LocalizedEnumValue> getColorInAllVariants(final ProductProjection product) {
//        return getAttributeInAllVariants(product, COLOR_ATTRIBUTE);
//    }
//
//    private <T> List<T> getAttributeInAllVariants(final ProductProjection product, final NamedAttributeAccess<T> accessor) {
//        return product.getAllVariants().stream()
//                .map(variant -> variant.findAttribute(accessor))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .distinct()
//                .collect(toList());
//    }
//
//    private SelectableData colorToSelectableItem(final LocalizedEnumValue color) {
//        final String colorLabel = color.getLabel().find(userContext.locales()).orElse("");
//        return new SelectableData(colorLabel, COLOR_ATTRIBUTE.getName());
//    }
//
//    private Optional<SelectableData> sizeToSelectableItem(final ProductVariant variant, final String slug, final String currentSize) {
//        return variant.findAttribute(SIZE_ATTRIBUTE).map(size -> {
//            final String url = reverseRouter.product(userContext.locale().getLanguage(), slug, variant.getSku()).url();
//            final SelectableData selectableData = new SelectableData(size, url);
//            if (size.equals(currentSize)) {
//                selectableData.setSelected(true);
//            }
//            return selectableData;
//        });
//    }
//
//    private DetailData localizedStringsToDetailData(final LocalizedEnumValue localizedStrings) {
//        final String label = localizedStrings.getLabel().find(userContext.locales()).orElse("");
//        return new DetailData(label);
//    }
//
//    private boolean productIsNew(final ProductProjection product, final CategoryTree categoryTreeNew) {
//        return product.getCategories().stream()
//                .anyMatch(category -> categoryTreeNew.findById(category.getId()).isPresent());
//    }
}
