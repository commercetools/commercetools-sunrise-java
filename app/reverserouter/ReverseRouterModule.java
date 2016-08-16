package reverserouter;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.*;
import com.google.inject.AbstractModule;

public class ReverseRouterModule extends AbstractModule {

    @Override
    protected void configure() {
        final ReverseRouterImpl reverseRouter = new ReverseRouterImpl();
        bind(ReverseRouter.class).toInstance(reverseRouter);
    }
}
