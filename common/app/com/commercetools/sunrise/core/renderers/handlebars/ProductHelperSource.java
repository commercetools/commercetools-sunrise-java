package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.models.products.AttributeViewModel;
import com.commercetools.sunrise.models.products.AttributeViewModelFactory;
import com.commercetools.sunrise.models.products.ProductAttributesSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.helper.StringHelpers;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductHelperSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHelperSource.class);

    private final AttributeViewModelFactory attributeViewModelFactory;
    private final ProductAttributesSettings attributesSettings;

    @Inject
    ProductHelperSource(final AttributeViewModelFactory attributeViewModelFactory,
                        final ProductAttributesSettings attributesSettings) {
        this.attributeViewModelFactory = attributeViewModelFactory;
        this.attributesSettings = attributesSettings;
    }

    public CharSequence displayedAttributes(final ProductVariant variant, final Reference<ProductType> productTypeRef,
                                            final Options options) {
        return applyTemplateFunctionForAttributes(variant, productTypeRef, options, attributesSettings.displayed());
    }

    public CharSequence selectableAttributes(final ProductVariant variant, final Reference<ProductType> productTypeRef,
                                             final Options options) {
        return applyTemplateFunctionForAttributes(variant, productTypeRef, options, attributesSettings.selectable());
    }

    public CharSequence attributeOptions(final Attribute selectedAttribute, final ProductProjection product, final Options options) {
        return distinctAttributesStream(selectedAttribute, product)
                .map(formattedAttribute -> applyTemplateFunction(formattedAttribute, options))
                .collect(joining());
    }

    private Stream<AttributeViewModel> distinctAttributesStream(final Attribute selectedAttribute, final ProductProjection product) {
        return product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(selectedAttribute.getName()))
                .filter(Objects::nonNull)
                .distinct()
                .map(attribute -> attributeViewModelFactory.create(attribute, product.getProductType(), selectedAttribute));
    }

    public CharSequence selectableAlgo(final Attribute selectedAttribute, final ProductProjection product, final Options options) throws JsonProcessingException {
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        distinctAttributesStream(selectedAttribute, product).forEach(attrOption -> {
            final String attrOptionValue = slugify(attrOption.getValue(), options).toString();
            selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attrOption, product.getAllVariants(), options));
        });
        return new ObjectMapper().writeValueAsString(selectableData);
    }

    private Map<String, List<String>> createAllowedAttributeCombinations(final AttributeViewModel fixedAttribute,
                                                                         final List<ProductVariant> variants, final Options options) {
        final Map<String, List<String>> attrCombination = new HashMap<>();
        attributesSettings.selectable().stream()
                .filter(enabledAttrKey -> !fixedAttribute.getName().equals(enabledAttrKey))
                .forEach(enabledAttrKey -> {
                    final List<String> allowedAttrValues = attributeCombination(enabledAttrKey, fixedAttribute, variants, options);
                    if (!allowedAttrValues.isEmpty()) {
                        attrCombination.put(enabledAttrKey, allowedAttrValues);
                    }
                });
        return attrCombination;
    }

    private List<String> attributeCombination(final String attributeKey, final AttributeViewModel fixedAttribute,
                                              final List<ProductVariant> variants, final Options options) {
        return variants.stream()
                .filter(variant -> {
                    final Attribute variantAttribute = variant.getAttribute(fixedAttribute.getName());
                    return variantAttribute != null && variantAttribute.equals(fixedAttribute);
                })
                .map(variant -> variant.getAttribute(attributeKey))
                .filter(Objects::nonNull)
                .map(attribute -> slugify(fixedAttribute.getValue(), options).toString())
                .distinct()
                .collect(toList());
    }

    private String applyTemplateFunctionForAttributes(final ProductVariant variant, final Reference<ProductType> productTypeRef,
                                                      final Options options, final List<String> attributeNames) {
        return attributeNames.stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> attributeViewModelFactory.create(attribute, productTypeRef, null))
                .map(formattedAttribute -> applyTemplateFunction(formattedAttribute, options))
                .collect(joining());
    }

    private CharSequence slugify(final String text, final Options options) {
        try {
            return StringHelpers.slugify.apply(text, options);
        } catch (IOException e) {
            LOGGER.error("Could not slugify text", e);
            return text;
        }
    }

    private CharSequence applyTemplateFunction(@Nullable final Object object, final Options options) {
        try {
            if (object != null) {
                return options.fn(object);
            }
        } catch (IOException e) {
            LOGGER.error("Could not apply the template function", e);
        }
        return "";
    }
}
