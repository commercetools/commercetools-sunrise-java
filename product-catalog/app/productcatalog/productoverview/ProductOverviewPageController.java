package productcatalog.productoverview;

import common.inject.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import productcatalog.productoverview.search.sort.SortSelectorComponent;

import javax.inject.Inject;

@RequestScoped
public class ProductOverviewPageController extends SunriseProductOverviewPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductOverviewPageController.class);

    @Inject
    public void setSortSelectorComponent(final SortSelectorComponent component) {
        registerControllerComponent(component);
    }
}