package com.commercetools.sunrise.framework.reverserouters.hololist;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

import javax.inject.Inject;

public class HololistLinksControllerComponent extends AbstractLinksControllerComponent<HololistReverseRouter> {

    private final HololistReverseRouter wishlistReverseRouter;

    @Inject
    protected HololistLinksControllerComponent(final HololistReverseRouter wishlistReverseRouter) {
        this.wishlistReverseRouter = wishlistReverseRouter;
    }

    @Override
    public HololistReverseRouter getReverseRouter() {
        return wishlistReverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final HololistReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.addToHololistProcess(), "addToHololist");
        meta.addHalLink(reverseRouter.removeFromHololistProcess(), "removeFromHololist");
        meta.addHalLink(reverseRouter.clearHololistProcess(), "clearHololist");
        meta.addHalLink(reverseRouter.hololistPageCall(), "myHololist");
    }
}
