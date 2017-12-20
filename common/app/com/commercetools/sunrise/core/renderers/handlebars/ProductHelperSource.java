package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.formatters.ProductAttributeFormatter;
import com.commercetools.sunrise.models.categories.CategoriesSettings;
import com.commercetools.sunrise.models.categories.NavigationCategoryTree;
import com.commercetools.sunrise.models.categories.NewCategoryTree;
import com.commercetools.sunrise.models.categories.SpecialCategorySettings;
import com.commercetools.sunrise.models.prices.PriceFactory;
import com.commercetools.sunrise.models.products.AttributeOption;
import com.commercetools.sunrise.models.products.AttributeOptionFactory;
import com.commercetools.sunrise.models.products.ProductAttributesSettings;
import com.commercetools.sunrise.models.products.ProductPriceUtils;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Call;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductHelperSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHelperSource.class);

    private final ProductAttributeFormatter productAttributeFormatter;
    private final AttributeOptionFactory attributeOptionFactory;
    private final ProductAttributesSettings attributesSettings;
    private final ProductTypeLocalRepository productTypeLocalRepository;
    private final PriceFactory priceFactory;
    private final ProductReverseRouter productReverseRouter;
    private final CategoryTree categoryTreeInNew;
    private final CategoryTree categoryTree;
    private final CategoriesSettings categoriesSettings;

    @Inject
    ProductHelperSource(final ProductAttributeFormatter productAttributeFormatter, final AttributeOptionFactory attributeOptionFactory,
                        final ProductAttributesSettings attributesSettings, final ProductTypeLocalRepository productTypeLocalRepository,
                        final PriceFactory priceFactory, final ProductReverseRouter productReverseRouter,
                        @NewCategoryTree final CategoryTree categoryTreeInNew, @NavigationCategoryTree final CategoryTree categoryTree,
                        final CategoriesSettings categoriesSettings) {
        this.productAttributeFormatter = productAttributeFormatter;
        this.attributeOptionFactory = attributeOptionFactory;
        this.attributesSettings = attributesSettings;
        this.productTypeLocalRepository = productTypeLocalRepository;
        this.priceFactory = priceFactory;
        this.productReverseRouter = productReverseRouter;
        this.categoryTreeInNew = categoryTreeInNew;
        this.categoryTree = categoryTree;
        this.categoriesSettings = categoriesSettings;
    }

    public CharSequence eachDisplayedAttribute(final Options options) {
        return attributesSettings.displayed().stream()
                .map(attributeName -> safeFnApply(attributeName, options))
                .collect(joining());
    }

    public CharSequence eachPrimarySelectableAttributeName(final Options options) {
        return attributesSettings.primarySelectable().stream()
                .map(attributeName -> safeFnApply(attributeName, options))
                .collect(joining());
    }

    public CharSequence eachSecondarySelectableAttributeName(final Options options) {
        return attributesSettings.secondarySelectable().stream()
                .map(attributeName -> safeFnApply(attributeName, options))
                .collect(joining());
    }

    public CharSequence withAttribute(final String attributeName, final ProductVariant variant, final Options options) {
        return Optional.ofNullable(variant.getAttribute(attributeName))
                .map(attribute -> safeFnApply(attribute, options))
                .orElse("");
    }

    public CharSequence attributeLabel(final String attributeName, @Nullable final Reference<ProductType> nullableProductTypeRef) {
        return productTypeOrDefault(attributeName, nullableProductTypeRef)
                .flatMap(productType -> productAttributeFormatter.label(attributeName, productType))
                .orElse(attributeName);
    }

    public CharSequence attributeValue(final Attribute attribute, @Nullable final Reference<ProductType> nullableProductTypeRef) {
        return productTypeOrDefault(attribute.getName(), nullableProductTypeRef)
                .flatMap(productTypeRef -> productAttributeFormatter.convert(attribute, productTypeRef))
                .orElseGet(() -> Json.stringify(attribute.getValueAsJsonNode()));
    }

    public CharSequence withAttributeOptions(final String attributeName, final ProductProjection product,
                                             final ProductVariant variant, final Options options) throws IOException {
        return options.fn(attributeOptions(attributeName, product, variant));
    }

    public CharSequence availabilityColorCode(final Long availableQuantity) {
        final String code;
        if (availableQuantity < 4) {
            code = "red";
        } else if (availableQuantity > 10) {
            code = "green";
        } else {
            code = "orange";
        }
        return code;
    }

    public CharSequence withProductPrice(final ProductVariant variant, final Options options) {
        return priceFactory.create(variant)
                .map(price -> safeFnApply(price, options))
                .orElse(null);
    }

    public CharSequence productUrl(final ProductProjection product, final ProductVariant variant) {
        return productReverseRouter.productDetailPageCall(product, variant).map(Call::url).orElse("");
    }

    public CharSequence categoryUrl(final Category category) {
        return productReverseRouter.productOverviewPageCall(category).map(Call::url).orElse("");
    }

    public CharSequence ifDiscounted(final ProductVariant variant, final Options options) throws IOException {
        final boolean discounted = ProductPriceUtils.hasDiscount(variant);
        return discounted ? options.fn() : null;
    }

    public CharSequence ifNew(final ProductProjection product, final Options options) throws IOException {
        final boolean isNew = product.getCategories().stream()
                .anyMatch(categoryRef -> categoryTreeInNew.findById(categoryRef.getId()).isPresent());
        return isNew ? options.fn() : null;
    }

    public CharSequence ifSale(final Category category, final Options options) throws IOException {
        final boolean isSale = categoriesSettings.specialCategories().stream()
                .filter(SpecialCategorySettings::isSale)
                .anyMatch(specialCategory -> specialCategory.externalId().equals(category.getExternalId()));
        return isSale ? options.fn() : null;
    }

    public CharSequence withCategoryChildren(final Category category, final Options options) throws IOException {
        final List<Category> children = categoryTree.findChildren(category);
        return options.fn(children);
    }

    private List<AttributeOption> attributeOptions(final String attributeName, final ProductProjection product,
                                                   final ProductVariant currentVariant) {
        return product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attributeName))
                .filter(Objects::nonNull)
                .distinct()
                .map(attribute -> attributeOptionFactory.create(attribute, product, currentVariant))
                .collect(toList());
    }

    private CharSequence safeFnApply(@Nullable final Object object, final Options options) {
        try {
            if (object != null) {
                return options.fn(object);
            }
        } catch (IOException e) {
            LOGGER.error("Could not apply the template function", e);
        }
        return "";
    }

    private Optional<ProductType> productTypeOrDefault(final String attributeName,
                                                       @Nullable final Reference<ProductType> nullableProductTypeRef) {
        return Optional.ofNullable(nullableProductTypeRef)
                .flatMap(productTypeRef -> productTypeLocalRepository.findById(productTypeRef.getId()))
                .map(Optional::of)
                .orElseGet(() -> productTypeLocalRepository.getAll().stream()
                        .filter(productType -> productType.findAttribute(attributeName).isPresent())
                        .findFirst());
    }
}
