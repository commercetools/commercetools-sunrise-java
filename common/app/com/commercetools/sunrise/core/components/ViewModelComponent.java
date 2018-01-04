package com.commercetools.sunrise.core.components;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

public class ViewModelComponent extends ViewModel implements Component {

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
