package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public abstract class HeroldComponentBase extends Base implements ControllerComponent, PageDataHook {
    @Inject
    private UserContext userContext;

    @Override
    public void acceptPageData(final PageData pageData) {
        updateMeta(pageData.getMeta());
    }

    protected void updateMeta(final PageMeta meta) {
    }

    protected String languageTag() {
        return userContext.languageTag();
    }
}
