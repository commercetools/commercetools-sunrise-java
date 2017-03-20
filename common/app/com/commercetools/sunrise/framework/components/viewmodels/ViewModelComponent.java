package com.commercetools.sunrise.framework.components.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.components.SunriseComponent;

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
