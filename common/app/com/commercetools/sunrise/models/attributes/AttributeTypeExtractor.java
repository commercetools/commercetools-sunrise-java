package com.commercetools.sunrise.models.attributes;

import io.sphere.sdk.products.attributes.*;

import java.util.Optional;
import java.util.function.Function;

public interface AttributeTypeExtractor<T> {

    Optional<T> get();

    <A extends AttributeType> AttributeTypeExtractor<T> ifIs(Class<A> attributeTypeClass, Function<A, ? extends T> function);

    default AttributeTypeExtractor<T> ifIsBoolean(Function<BooleanAttributeType, ? extends T> function) {
        return ifIs(BooleanAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsDate(Function<DateAttributeType, ? extends T> function) {
        return ifIs(DateAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsDateTime(Function<DateTimeAttributeType, ? extends T> function) {
        return ifIs(DateTimeAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsEnum(Function<EnumAttributeType, ? extends T> function) {
        return ifIs(EnumAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsNumber(Function<NumberAttributeType, ? extends T> function) {
        return ifIs(NumberAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsLocalizedEnum(Function<LocalizedEnumAttributeType, ? extends T> function) {
        return ifIs(LocalizedEnumAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsLocalizedString(Function<LocalizedStringAttributeType, ? extends T> function) {
        return ifIs(LocalizedStringAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsMoney(Function<MoneyAttributeType, ? extends T> function) {
        return ifIs(MoneyAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsReference(Function<ReferenceAttributeType, ? extends T> function) {
        return ifIs(ReferenceAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsSet(Function<SetAttributeType, ? extends T> function) {
        return ifIs(SetAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsString(Function<StringAttributeType, ? extends T> function) {
        return ifIs(StringAttributeType.class, function);
    }

    default AttributeTypeExtractor<T> ifIsTime(Function<TimeAttributeType, ? extends T> function) {
        return ifIs(TimeAttributeType.class, function);
    }

    static <T> AttributeTypeExtractor<T> of(AttributeDefinition attributeDefinition) {
        return new AttributeTypeExtractorImpl<>(attributeDefinition.getAttributeType(), null);
    }
}
