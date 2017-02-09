package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.LocalizedString;

@ImplementedBy(AttributeFormatterImpl.class)
public interface AttributeFormatter {

    LocalizedString label(final AttributeWithProductType attributeWithProductType);

    String value(final AttributeWithProductType attributeWithProductType);

    String valueAsKey(final AttributeWithProductType attributeWithProductType);
}
