package com.commercetools.sunrise.core.i18n;

import com.commercetools.sunrise.models.SelectOption;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(LanguageSelectorImpl.class)
public interface LanguageSelector {

    List<SelectOption> options();
}
