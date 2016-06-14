package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;

import javax.inject.Inject;

public class PageNavMenuFactoryImpl implements PageNavMenuFactory {

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
    public PageNavMenu create() {
        return new PageNavMenu(categoryTree, userContext, reverseRouter, saleCategoryExtId);
    }
}
