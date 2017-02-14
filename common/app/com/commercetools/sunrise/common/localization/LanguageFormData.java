package com.commercetools.sunrise.common.localization;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

import java.util.Locale;

public class LanguageFormData extends Base {

    @Constraints.Required
    private String language;

    public String getLanguage() {
        return language;
    }

    public Locale toLocale() {
        return Locale.forLanguageTag(language);
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}
