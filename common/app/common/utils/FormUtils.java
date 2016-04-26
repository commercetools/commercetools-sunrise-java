package common.utils;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import play.data.Form;

import javax.annotation.Nullable;

public final class FormUtils {

    private FormUtils() {
    }

    public static Address extractAddress(@Nullable final Form<?> form, final String suffix) {
        if (form != null) {
            final CountryCode country = countryOrUndefined(form.field("country" + suffix).value());
            return AddressBuilder.of(country)
                    .title(form.field("title" + suffix).value())
                    .firstName(form.field("firstName" + suffix).value())
                    .lastName(form.field("lastName" + suffix).value())
                    .streetName(form.field("streetName" + suffix).value())
                    .additionalStreetInfo(form.field("additionalStreetInfo" + suffix).value())
                    .city(form.field("city" + suffix).value())
                    .postalCode(form.field("postalCode" + suffix).value())
                    .region(form.field("region" + suffix).value())
                    .phone(form.field("phone" + suffix).value())
                    .email(form.field("email" + suffix).value())
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
