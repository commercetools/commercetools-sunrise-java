package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

import java.util.List;

public class AttributeSelector extends ViewModel {

    private boolean reload;
    private List<AttributeOption> options;

    public boolean isReload() {
        return reload;
    }

    public void setReload(final boolean reload) {
        this.reload = reload;
    }

    public List<AttributeOption> getOptions() {
        return options;
    }

    public void setOptions(final List<AttributeOption> options) {
        this.options = options;
    }
}
