package reverserouter;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;

public class ReverseRouterProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        final ReverseRouterImpl reverseRouter = new ReverseRouterImpl();
        bind(ReverseRouter.class).toInstance(reverseRouter);
        bind(ProductReverseRouter.class).toInstance(reverseRouter);
        bind(CheckoutReverseRouter.class).toInstance(reverseRouter);
        bind(HomeReverseRouter.class).toInstance(reverseRouter);
    }
}
