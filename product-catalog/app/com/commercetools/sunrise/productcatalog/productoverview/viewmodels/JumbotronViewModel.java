package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.TitleDescriptionViewModel;
import io.sphere.sdk.models.LocalizedString;

public class JumbotronViewModel extends TitleDescriptionViewModel {

    private LocalizedString subtitle;

    public JumbotronViewModel() {
    }

    public LocalizedString getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final LocalizedString subtitle) {
        this.subtitle = subtitle;
    }
}
