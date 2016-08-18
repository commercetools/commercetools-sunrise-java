package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;

import javax.inject.Inject;

final class SunriseMyPersonalDetailsHeroldComponent extends HeroldComponentBase {
    @Inject
    private MyPersonalDetailsReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.myPersonalDetailsPageCall(languageTag()), "myPersonalDetails", "myAccount");
        meta.addHalLink(reverseRouter.myPersonalDetailsProcessFormCall(languageTag()), "editMyPersonalDetails");
    }
}
