package wedecidelater;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.controllers.PageMetaFactory;
import com.commercetools.sunrise.common.models.NavMenuDataFactory;

public class FrameworkWireModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PageMetaFactory.class).to(PageMetaFactoryImpl.class);
        bind(NavMenuDataFactory.class).to(NavMenuDataFactoryImpl.class);
    }
}
