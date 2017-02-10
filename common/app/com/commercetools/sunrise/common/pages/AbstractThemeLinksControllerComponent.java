package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import io.sphere.sdk.models.Base;

public abstract class AbstractThemeLinksControllerComponent extends Base implements ControllerComponent, PageDataReadyHook {

    @Override
    public void onPageDataReady(final PageData pageData) {
        addThemeLinks(pageData.getMeta());
    }

    protected abstract void addThemeLinks(final PageMeta meta);
}
