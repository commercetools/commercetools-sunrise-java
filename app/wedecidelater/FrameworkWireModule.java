package wedecidelater;

import com.google.inject.AbstractModule;
import common.controllers.PageMetaFactory;
import common.models.NavMenuDataFactory;

public class FrameworkWireModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PageMetaFactory.class).to(PageMetaFactoryImpl.class);
        bind(NavMenuDataFactory.class).to(NavMenuDataFactoryImpl.class);
    }
}
