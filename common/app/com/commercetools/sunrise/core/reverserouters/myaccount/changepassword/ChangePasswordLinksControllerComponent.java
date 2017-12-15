package com.commercetools.sunrise.core.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.core.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

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
