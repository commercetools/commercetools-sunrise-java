package wedecidelater;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.NavMenuData;
import common.models.NavMenuDataFactory;
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
