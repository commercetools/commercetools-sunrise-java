package com.commercetools.sunrise.components;

import io.sphere.sdk.models.Base;

public class ComponentBean extends Base {
    private String templateName;
    private Object componentData;

    public ComponentBean() {
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
