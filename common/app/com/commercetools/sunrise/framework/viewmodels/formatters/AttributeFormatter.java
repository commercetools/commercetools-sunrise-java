package com.commercetools.sunrise.framework.viewmodels.formatters;

import com.commercetools.sunrise.framework.viewmodels.content.products.AttributeWithProductType;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.LocalizedString;

import javax.annotation.Nullable;

@ImplementedBy(AttributeFormatterImpl.class)
public interface AttributeFormatter {

    /**
     * Obtains the label corresponding to the given attribute from this product type.
     * @param attributeWithProductType attribute with its corresponding product type
     * @return the label of this attribute in different locales
     */
    LocalizedString label(@Nullable final AttributeWithProductType attributeWithProductType);

    /**
     * Obtains the value (i.e. {@code name} in CTP terminology) corresponding to the given attribute from this product type.
     * @param attributeWithProductType attribute with its corresponding product type
     * @return the value of this attribute
     */
    String value(@Nullable final AttributeWithProductType attributeWithProductType);

    /**
     * Obtains a white-listed version of the {@link #value(AttributeWithProductType)}, ready to be used for HTTP requests.
     * @param attributeWithProductType attribute with its corresponding product type
     * @return the encoded value of this attribute
     */
    String encodedValue(@Nullable final AttributeWithProductType attributeWithProductType);
}
