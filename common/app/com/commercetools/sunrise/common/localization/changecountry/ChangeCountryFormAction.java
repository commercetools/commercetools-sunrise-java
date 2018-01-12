package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

import java.util.function.Consumer;

@ImplementedBy(DefaultChangeCountryFormAction.class)
@FunctionalInterface
public interface ChangeCountryFormAction extends FormAction, Consumer<ChangeCountryFormData> {

    @Override
    void accept(ChangeCountryFormData formData);
}
