package productcatalog.productoverview;

import common.inject.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

@RequestScoped
public class ProductOverviewPageController extends SunriseProductOverviewPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductOverviewPageController.class);

    @Override
    public Set<String> getFrameworkTags() {
        return Collections.emptySet();
    }
}