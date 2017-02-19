package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.common.models.TitleDescriptionBean;
import io.sphere.sdk.models.LocalizedString;

public class JumbotronBean extends TitleDescriptionBean {

    private LocalizedString subtitle;

    public JumbotronBean() {
    }

    public LocalizedString getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final LocalizedString subtitle) {
        this.subtitle = subtitle;
    }
}
