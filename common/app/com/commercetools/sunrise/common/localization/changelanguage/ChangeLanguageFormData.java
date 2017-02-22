package com.commercetools.sunrise.common.localization.changelanguage;

import com.google.inject.ImplementedBy;

import java.util.Locale;

@ImplementedBy(DefaultChangeLanguageFormData.class)
public interface ChangeLanguageFormData {

    Locale locale();
}
