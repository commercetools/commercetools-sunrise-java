package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.RecoverPasswordReverseRouter;

import javax.inject.Inject;

final class SunriseRecoverPasswordHeroldComponent extends HeroldComponentBase {
    @Inject
    private RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(recoverPasswordReverseRouter.processRequestRecoverEmailForm(languageTag()), "recoveryEmail");
    }
}
