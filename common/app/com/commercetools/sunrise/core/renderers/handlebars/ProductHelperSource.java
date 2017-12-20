package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.core.viewmodels.formatters.ProductAttributeFormatter;
import com.commercetools.sunrise.models.products.AttributeOptionViewModelFactory;
import com.commercetools.sunrise.models.products.ProductAttributesSettings;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Singleton
public class ProductHelperSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHelperSource.class);

    private final ProductAttributeFormatter productAttributeFormatter;
    private final AttributeOptionViewModelFactory attributeOptionViewModelFactory;
    private final ProductAttributesSettings attributesSettings;
    private final ProductTypeLocalRepository productTypeLocalRepository;

    @Inject
    ProductHelperSource(final ProductAttributeFormatter productAttributeFormatter, final AttributeOptionViewModelFactory attributeOptionViewModelFactory,
                        final ProductAttributesSettings attributesSettings, final ProductTypeLocalRepository productTypeLocalRepository) {
        this.productAttributeFormatter = productAttributeFormatter;
        this.attributeOptionViewModelFactory = attributeOptionViewModelFactory;
        this.attributesSettings = attributesSettings;
        this.productTypeLocalRepository = productTypeLocalRepository;
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
        return productTypeOrDefault(nullableProductTypeRef)
                .flatMap(productType -> productAttributeFormatter.label(attributeName, productType))
                .orElse(attributeName);
    }

    public CharSequence attributeValue(final Attribute selectedAttribute, @Nullable final Reference<ProductType> nullableProductTypeRef) {
        return productTypeOrDefault(nullableProductTypeRef)
                .flatMap(productTypeRef -> productAttributeFormatter.convert(selectedAttribute, productTypeRef))
                .orElseGet(() -> Json.stringify(selectedAttribute.getValueAsJsonNode()));
    }

    public CharSequence eachAttributeOption(final String attributeName, final ProductProjection product,
                                            final ProductVariant variant, final Options options) {
        return distinctAttributeValuesStream(attributeName, product)
                .map(attribute -> attributeOptionViewModelFactory.create(attribute, product, variant))
                .map(attribute -> safeFnApply(attribute, options))
                .collect(joining());
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

    private Stream<Attribute> distinctAttributeValuesStream(final String attributeName, final ProductProjection product) {
        return product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attributeName))
                .filter(Objects::nonNull)
                .distinct();
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

    private Optional<ProductType> productTypeOrDefault(@Nullable final Reference<ProductType> nullableProductTypeRef) {
        return Optional.ofNullable(nullableProductTypeRef)
                .flatMap(productTypeRef -> productTypeLocalRepository.findById(productTypeRef.getId()))
                .map(Optional::of)
                .orElseGet(() -> productTypeLocalRepository.getAll().stream().findFirst());
    }
}
