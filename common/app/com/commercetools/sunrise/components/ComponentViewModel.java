package com.commercetools.sunrise.components;

import com.commercetools.sunrise.common.models.ViewModel;

public class ComponentViewModel extends ViewModel {

    private String templateName;
    private Object componentData;

    public ComponentViewModel() {
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public Object getComponentData() {
        return componentData;
    }

    public void setComponentData(final Object componentData) {
        this.componentData = componentData;
    }
}
