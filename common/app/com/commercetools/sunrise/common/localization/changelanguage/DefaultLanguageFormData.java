package com.commercetools.sunrise.common.localization.changelanguage;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

import java.util.Locale;

public class DefaultLanguageFormData extends Base implements LanguageFormData {

    @Constraints.Required
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    @Override
    public Locale toLocale() {
        return Locale.forLanguageTag(language);
    }
}
