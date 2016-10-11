package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public abstract class HeroldComponentBase extends Base implements ControllerComponent, PageDataReadyHook {
    @Inject
    private UserContext userContext;

    @Override
    public void onPageDataReady(final PageData pageData) {
        updateMeta(pageData.getMeta());
    }

    protected void updateMeta(final PageMeta meta) {
    }

    protected String languageTag() {
        return userContext.languageTag();
    }
}
