package reverserouter;

import com.google.inject.AbstractModule;
import common.controllers.ReverseRouter;

public class ReverseRouterProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ReverseRouter.class).toInstance(new ReverseRouterImpl());
    }
}
