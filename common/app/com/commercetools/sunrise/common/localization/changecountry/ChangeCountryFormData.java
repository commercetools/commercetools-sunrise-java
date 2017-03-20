package com.commercetools.sunrise.common.localization.changecountry;

import com.google.inject.ImplementedBy;
import com.neovisionaries.i18n.CountryCode;

@ImplementedBy(DefaultChangeCountryFormData.class)
public interface ChangeCountryFormData {

    CountryCode countryCode();
}
