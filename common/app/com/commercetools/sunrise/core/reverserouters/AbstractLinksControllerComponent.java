package com.commercetools.sunrise.core.reverserouters;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.OldPageData;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;
import io.sphere.sdk.models.Base;

public abstract class AbstractLinksControllerComponent<T> extends Base implements ControllerComponent, PageDataHook {

    @Override
    public final void onPageDataReady(final OldPageData oldPageData) {
        addLinksToPage(oldPageData.getMeta(), getReverseRouter());
    }

    public abstract T getReverseRouter();

    protected abstract void addLinksToPage(final PageMeta meta, final T reverseRouter);
}
