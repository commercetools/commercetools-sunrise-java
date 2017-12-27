package com.commercetools.sunrise.models.attributes;

import io.sphere.sdk.products.attributes.AttributeType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

final class AttributeTypeExtractorImpl<T> implements AttributeTypeExtractor<T> {

    private final AttributeType attributeType;
    @Nullable
    private final T value;

    AttributeTypeExtractorImpl(final AttributeType attributeType, @Nullable final T value) {
        this.attributeType = attributeType;
        this.value = value;
    }

    @Override
    public Optional<T> get() {
        return Optional.ofNullable(value);
    }

    @Override
    public <A extends AttributeType> AttributeTypeExtractor<T> ifIs(final Class<A> attributeTypeClass, final Function<A, ? extends T> function) {
        return Optional.ofNullable(this.value)
                .map(x -> this)
                .orElseGet(() -> tryWithType(attributeTypeClass, function));
    }

    private <A> AttributeTypeExtractorImpl<T> tryWithType(final Class<A> attributeTypeClass, final Function<A, ? extends T> function) {
        if (attributeType.getClass().equals(attributeTypeClass)) {
            final T value = function.apply(attributeTypeClass.cast(attributeType));
            return new AttributeTypeExtractorImpl<>(attributeType, value);
        }
        return this;
    }
}
