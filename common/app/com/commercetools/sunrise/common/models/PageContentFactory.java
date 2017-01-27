package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.pages.PageContent;

public abstract class PageContentFactory<T extends PageContent, D extends ControllerData> extends ViewModelFactory<T, D> {

    @Override
    protected void initialize(final T model, final D data) {
        fillTitle(model, data);
    }

    protected abstract void fillTitle(final T model, final D data);
}
