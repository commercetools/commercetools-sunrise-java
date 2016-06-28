package com.commercetools.sunrise.common.forms;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class FormUtils {

    private FormUtils() {
    }

    @Nullable
    public static String extractFormField(@Nullable final Form<?> form, final String fieldName) {
        return form != null ? form.field(fieldName).value() : null;
    }

    public static Function<String, String> formFieldExtractor(@Nullable final Form<?> form) {
        return formFieldExtractor(form, "");
    }

    public static Function<String, String> formFieldExtractor(@Nullable final Form<?> form, final String suffix) {
        return fieldName -> extractFormField(form, fieldName + suffix);
    }

    public static boolean extractBooleanFormField(@Nullable final Form<?> form, final String fieldName) {
        return Boolean.valueOf(extractFormField(form, fieldName));
    }

    public static Address extractAddress(@Nullable final Form<?> form) {
        return extractAddress(form, "");
    }

    public static Address extractAddress(@Nullable final Form<?> form, final String suffix) {
        if (form != null) {
            final Function<String, String> formExtractor = formFieldExtractor(form, suffix);
            final CountryCode country = countryOrUndefined(formExtractor.apply("country"));
            return AddressBuilder.of(country)
                    .title(formExtractor.apply("title"))
                    .firstName(formExtractor.apply("firstName"))
                    .lastName(formExtractor.apply("lastName"))
                    .streetName(formExtractor.apply("streetName"))
                    .additionalStreetInfo(formExtractor.apply("additionalStreetInfo"))
                    .city(formExtractor.apply("city"))
                    .postalCode(formExtractor.apply("postalCode"))
                    .region(formExtractor.apply("region"))
                    .phone(formExtractor.apply("phone"))
                    .email(formExtractor.apply("email"))
                    .build();
        } else {
            return Address.of(CountryCode.UNDEFINED);
        }
    }

    private static CountryCode countryOrUndefined(@Nullable final String selectedCountry) {
        final CountryCode country = CountryCode.getByCode(selectedCountry);
        return country != null ? country : CountryCode.UNDEFINED;
    }
}
