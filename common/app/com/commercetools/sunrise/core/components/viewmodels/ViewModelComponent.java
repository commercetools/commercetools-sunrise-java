package com.commercetools.sunrise.core.components.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import com.commercetools.sunrise.core.components.SunriseComponent;

public class ViewModelComponent extends ViewModel implements SunriseComponent {

    private String templateName;

    public ViewModelComponent() {
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }
}
