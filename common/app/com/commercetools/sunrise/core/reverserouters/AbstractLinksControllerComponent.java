package com.commercetools.sunrise.core.reverserouters;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;
import io.sphere.sdk.models.Base;

public abstract class AbstractLinksControllerComponent<T> extends Base implements ControllerComponent, PageDataReadyHook {

    @Override
    public final void onPageDataReady(final PageData pageData) {
        addLinksToPage(pageData.getMeta(), getReverseRouter());
    }

    public abstract T getReverseRouter();

    protected abstract void addLinksToPage(final PageMeta meta, final T reverseRouter);
}
