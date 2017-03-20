package com.commercetools.sunrise.framework.reverserouters;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;
import io.sphere.sdk.models.Base;

public abstract class AbstractLinksControllerComponent<T> extends Base implements ControllerComponent, PageDataReadyHook {

    @Override
    public final void onPageDataReady(final PageData pageData) {
        addLinksToPage(pageData.getMeta(), getReverseRouter());
    }

    public abstract T getReverseRouter();

    protected abstract void addLinksToPage(final PageMeta meta, final T reverseRouter);
}
