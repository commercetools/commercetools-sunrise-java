package com.commercetools.sunrise.common.localization.changelanguage;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

import java.util.function.Consumer;

@ImplementedBy(DefaultChangeLanguageFormAction.class)
@FunctionalInterface
public interface ChangeLanguageFormAction extends FormAction, Consumer<ChangeLanguageFormData> {

    @Override
    void accept(ChangeLanguageFormData formData);
}
