package wedecidelater;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.models.NavMenuData;
import com.commercetools.sunrise.common.models.NavMenuDataFactory;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;

import javax.inject.Inject;

public class NavMenuDataFactoryImpl implements NavMenuDataFactory {
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private UserContext userContext;
    @Inject
    private ReverseRouter reverseRouter;
    private String saleCategoryExtId;
    @Inject
    public void initializeFields(Configuration configuration) {
        this.saleCategoryExtId = configuration.getString("common.saleCategoryExternalId");
    }

    @Override
    public NavMenuData create() {
        return new NavMenuData(categoryTree, userContext, reverseRouter, saleCategoryExtId);
    }
}
