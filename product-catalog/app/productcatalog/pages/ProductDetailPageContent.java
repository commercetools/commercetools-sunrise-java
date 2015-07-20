package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.*;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.attributes.Attribute;
import io.sphere.sdk.attributes.AttributeAccess;
import io.sphere.sdk.models.LocalizedEnumValue;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.productdiscounts.DiscountedPrice;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.money.MonetaryAmount;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static common.utils.Languages.translate;
import static java.util.Arrays.asList;

public class ProductDetailPageContent extends PageContent {

    private static final int SHORT_DESCRIPTION_MAX_CHARACTERS = 170;

    private final AppContext context;
    private final PriceFormatter priceFormatter;
    private final ProductProjection product;
    private final List<ProductProjection> suggestionList;
    private final List<ShippingMethod> shippingMethods;
    private final Optional<Price> priceOpt;

    public ProductDetailPageContent(final CmsPage cms, final AppContext context, final ProductProjection product, List<ProductProjection> suggestionList, final List<ShippingMethod> shippingMethods, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        super(cms);
        this.context = context;
        this.priceFormatter = priceFormatter;
        this.product = product;
        this.suggestionList = suggestionList;
        this.shippingMethods = shippingMethods;
        this.priceOpt = priceFinder.findPrice(product.getMasterVariant().getPrices());
    }

    @Override
    public String additionalTitle() {
        return translate(product.getName(), context);
    }

    public String getText() {
        return cms.getOrEmpty("content.text");
    }

    public List<ImageData> getGallery() {
        return product.getMasterVariant().getImages().stream()
                .map(ImageData::of)
                .collect(Collectors.toList());
    }

    public ProductData getProduct() {
        return ProductDataBuilder.of()
                .withText(translate(product.getName(), context))
                .withSku(product.getMasterVariant().getSku().orElse(""))
                .withRatingList(buildRating())
                .withDescription(product.getDescription().map(description -> shorten(translate(description, context))).orElse(""))
                .withAdditionalDescription(product.getDescription().map(description -> translate(description, context)).orElse(""))
                .withViewDetailsText(cms.getOrEmpty("product.viewDetails"))
                .withPrice(getFormattedPrice())
                .withPriceOld(getFormattedPriceOld())
                .withColorList(buildColorList())
                .withSizeList(buildSizeList())
                .withSizeGuideText(cms.getOrEmpty("product.sizeGuide"))
                .withBagItemList(buildBagItemList())
                .withAddToBagText(cms.getOrEmpty("product.addToBag"))
                .withAddToWishlistText(cms.getOrEmpty("product.addToWishlist"))
                .withAvailableText(cms.getOrEmpty("product.available"))
                .withProductDetails(buildProductDetails())
                .withDeliveryAndReturn(buildDeliveryAndReturn())
                .build();
    }

    public ProductListData getSuggestions() {
        return new ProductListData(suggestionList, context, priceFormatter,
                cms.getOrEmpty("suggestions.text"), cms.getOrEmpty("suggestions.sale"),
                cms.getOrEmpty("suggestions.new"), cms.getOrEmpty("suggestions.quickView"),
                cms.getOrEmpty("suggestions.wishlist"), cms.getOrEmpty("suggestions.moreColors"));
    }

    public CollectionData getReviews() {
        return new CollectionData(cms.getOrEmpty("reviews.text"), Collections.<SelectableData>emptyList());
    }

    private CollectionData buildRating() {
        return new CollectionData("4/5", asList(
                new SelectableData("5 Stars", "5", cms.getOrEmpty("product.ratingList.five.text"), "", false),
                new SelectableData("4 Stars", "4", cms.getOrEmpty("product.ratingList.four.text"), "", false),
                new SelectableData("3 Stars", "3", cms.getOrEmpty("product.ratingList.three.text"), "", false),
                new SelectableData("2 Stars", "2", cms.getOrEmpty("product.ratingList.two.text"), "", false),
                new SelectableData("1 Stars", "1", cms.getOrEmpty("product.ratingList.one.text"), "", false)
        ));
    }

    private CollectionData buildColorList() {
        final AttributeAccess<LocalizedEnumValue> access = AttributeAccess.ofLocalizedEnumValue();
        final List<SelectableData> colors = product.getAllVariants().stream()
                .map(variant -> variant.getAttribute("color")).filter(Optional::isPresent).map(Optional::get).distinct()
                .map(color -> selectableColor(color, access))
                .collect(Collectors.toList());

        colors.add(0, new SelectableData(cms.getOrEmpty("product.colorList.choose.text"), "none", "", "", true));

        return new CollectionData(cms.getOrEmpty("product.colorList.text"), colors);
    }

    private SelectableData selectableColor(final Attribute color, final AttributeAccess<LocalizedEnumValue> access) {
        return new SelectableData(
                translate(color.getValue(access).getLabel(), context), color.getName(), "", "", false);
    }

    private CollectionData buildSizeList() {
        final AttributeAccess<String> access = AttributeAccess.ofText();
        final List<SelectableData> sizes = product.getAllVariants().stream()
                .map(variant -> variant.getAttribute("size")).filter(Optional::isPresent).map(Optional::get).distinct()
                .map(size -> selectableSize(size, access))
                .collect(Collectors.toList());

        sizes.add(0, new SelectableData(cms.getOrEmpty("product.sizeList.choose.text"), "none", "", "", true));

        return new CollectionData(cms.getOrEmpty("product.sizeList.text"), sizes);
    }

    private SelectableData selectableSize(final Attribute size, final AttributeAccess<String> access) {
        return new SelectableData(size.getValue(access), size.getValue(access), "", "", false);
    }

    private CollectionData buildBagItemList() {
        final List<SelectableData> items = IntStream.range(2, 10)
                .mapToObj(n -> new SelectableData(Integer.toString(n), "", "", "", false))
                .collect(Collectors.toList());

        items.add(0, new SelectableData("1", "", "", "", true));

        return new CollectionData("", items);
    }

    private DetailData buildProductDetails() {
        final Optional<Set<LocalizedStrings>> detailsOpt =
                product.getMasterVariant().getAttribute("details", AttributeAccess.ofLocalizedStringsSet());

        return new DetailData(cms.getOrEmpty("product.productDetails.text"), concatDetails(detailsOpt));
    }

    private String concatDetails (final Optional<Set<LocalizedStrings>> detailsOpt) {
        return detailsOpt.map(details -> String.join(", ", details.stream()
                .map(detail -> translate(detail, context))
                .collect(Collectors.toList())))
                .orElse("");
    }

    private DetailData buildDeliveryAndReturn() {
        final String desc = shippingMethods.stream().findFirst().map(ShippingMethod::getName).orElse("");
        return new DetailData(cms.getOrEmpty("product.deliveryAndReturn.text"), desc);
    }

    private String getFormattedPrice() {
        final Optional<MonetaryAmount> currentPrice = priceOpt.map(price -> price.getDiscounted()
                .map(DiscountedPrice::getValue)
                .orElse(price.getValue()));

        return currentPrice.map(price -> priceFormatter.format(price, context)).orElse("");
    }

    private String getFormattedPriceOld() {
        final Optional<MonetaryAmount> oldPrice = priceOpt.flatMap(price -> price.getDiscounted()
                .map(d -> price.getValue()));

        return oldPrice.map(price -> priceFormatter.format(price, context)).orElse("");
    }

    private String shorten(final String text) {
        return text.substring(0, SHORT_DESCRIPTION_MAX_CHARACTERS);
    }
}