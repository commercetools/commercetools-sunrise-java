package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.core.viewmodels.formatters.AttributeFormatter;
import com.commercetools.sunrise.models.products.AttributeWithProductType;
import com.commercetools.sunrise.models.products.ProductAttributesSettings;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@Singleton
public class ProductHelperSource {

    private final AttributeFormatter attributeFormatter;
    private final ProductAttributesSettings productAttributesSettings;

    @Inject
    ProductHelperSource(final AttributeFormatter attributeFormatter, final ProductAttributesSettings productAttributesSettings) {
        this.attributeFormatter = attributeFormatter;
        this.productAttributesSettings = productAttributesSettings;
    }

    public CharSequence attributes(final ProductVariant variant, final Options options) {
        return productAttributesSettings.displayed().stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .map(context -> {
                    try {
                        return options.fn(context);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(joining());
    }

    public CharSequence attribute(final Attribute attribute, final Reference<ProductType> productTypeRef) {
        return attributeFormatter.value(AttributeWithProductType.of(attribute, productTypeRef));
    }
}
