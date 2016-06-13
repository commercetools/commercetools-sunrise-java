package com.commercetools.sunrise;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.controllers.ReverseRouter;

public class ReverseRouterTestModule extends AbstractModule {

    private final ReverseRouter reverseRouter;

    public ReverseRouterTestModule(final ReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void configure() {
        bind(ReverseRouter.class).toInstance(reverseRouter);
    }
}
