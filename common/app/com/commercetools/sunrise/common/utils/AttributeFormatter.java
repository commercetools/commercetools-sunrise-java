package com.commercetools.sunrise.common.utils;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.attributes.Attribute;

@ImplementedBy(AttributeFormatterImpl.class)
public interface AttributeFormatter {

    String label(final Attribute attribute);

    String value(final Attribute attribute);

    String valueAsKey(final Attribute attribute);
}
