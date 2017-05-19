package com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

import javax.inject.Inject;

public class ChangePasswordLinksControllerComponent extends AbstractLinksControllerComponent<ChangePasswordReverseRouter> {

    private final ChangePasswordReverseRouter reverseRouter;

    @Inject
    protected ChangePasswordLinksControllerComponent(final ChangePasswordReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final ChangePasswordReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final ChangePasswordReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.changePasswordProcessCall(), "changePassword");
    }
}
