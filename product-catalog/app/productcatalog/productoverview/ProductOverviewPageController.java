package productcatalog.productoverview;

import common.controllers.ControllerDependency;
import common.inject.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@RequestScoped
public class ProductOverviewPageController extends SunriseProductOverviewPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductOverviewPageController.class);

    @Inject
    public ProductOverviewPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }
}