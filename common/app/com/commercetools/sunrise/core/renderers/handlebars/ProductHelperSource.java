package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.viewmodels.formatters.AttributeFormatter;
import com.commercetools.sunrise.models.products.AttributeWithProductType;
import com.commercetools.sunrise.models.products.ProductAttributesSettings;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@Singleton
public class ProductHelperSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHelperSource.class);

    private final I18nResolver i18nResolver;
    private final AttributeFormatter attributeFormatter;
    private final ProductAttributesSettings productAttributesSettings;

    @Inject
    ProductHelperSource(final I18nResolver i18nResolver, final AttributeFormatter attributeFormatter,
                        final ProductAttributesSettings productAttributesSettings) {
        this.i18nResolver = i18nResolver;
        this.attributeFormatter = attributeFormatter;
        this.productAttributesSettings = productAttributesSettings;
    }

    public CharSequence displayedAttributes(final ProductVariant variant, final Options options) {
        return productAttributesSettings.displayed().stream()
                .map(variant::getAttribute)
                .map(attribute -> applyTemplateFunction(attribute, options))
                .filter(Objects::nonNull)
                .collect(joining());
    }

    public CharSequence printAttributeLabel(final Attribute attribute, final Reference<ProductType> productTypeRef) {
        final LocalizedString label = attributeFormatter.label(AttributeWithProductType.of(attribute, productTypeRef));
        return i18nResolver.get(label).orElse("");
    }

    public CharSequence printAttributeValue(final Attribute attribute, final Reference<ProductType> productTypeRef) {
        return attributeFormatter.value(AttributeWithProductType.of(attribute, productTypeRef));
    }

    @Nullable
    private CharSequence applyTemplateFunction(@Nullable final Object object, final Options options) {
        try {
            if (object != null) {
                return options.fn(object);
            }
        } catch (IOException e) {
            LOGGER.error("Could not apply the template function", e);
        }
        return null;
    }
}
