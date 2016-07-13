package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.InfoData;

public class JumbotronBean extends InfoData {

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
