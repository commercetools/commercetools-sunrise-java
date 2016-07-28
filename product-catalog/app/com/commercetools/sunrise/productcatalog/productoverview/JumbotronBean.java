package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.TitleDescriptionBean;

public class JumbotronBean extends TitleDescriptionBean {

    private String subtitle;

    public JumbotronBean() {
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }
}
