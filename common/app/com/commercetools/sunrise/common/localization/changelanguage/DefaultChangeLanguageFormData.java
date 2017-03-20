package com.commercetools.sunrise.common.localization.changelanguage;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

import java.util.Locale;

public class DefaultChangeLanguageFormData extends Base implements ChangeLanguageFormData {

    @Required
    private String language;

    @Override
    public Locale locale() {
        return Locale.forLanguageTag(language);
    }


    // Getters & setters

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}
