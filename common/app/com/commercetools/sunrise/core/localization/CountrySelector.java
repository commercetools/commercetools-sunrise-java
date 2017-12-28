package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.models.SelectOption;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(CountrySelectorImpl.class)
public interface CountrySelector {

    List<SelectOption> options();
}
