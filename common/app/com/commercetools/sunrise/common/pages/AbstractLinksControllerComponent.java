package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import io.sphere.sdk.models.Base;

public abstract class AbstractLinksControllerComponent<T> extends Base implements ControllerComponent, PageDataReadyHook {

    @Override
    public final void onPageDataReady(final PageData pageData) {
        addLinksToPage(pageData.getMeta(), getReverseRouter());
    }

    public abstract T getReverseRouter();

    protected abstract void addLinksToPage(final PageMeta meta, final T reverseRouter);
}
