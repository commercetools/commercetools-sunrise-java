package com.commercetools.sunrise.common.localization;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class LanguageFormData extends Base {

    @Constraints.Required
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}
