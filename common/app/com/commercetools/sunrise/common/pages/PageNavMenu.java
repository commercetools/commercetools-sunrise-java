package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.models.categories.CategoryViewModel;

import java.util.List;

public class PageNavMenu extends ViewModel {

    private List<CategoryViewModel> categories;

    public PageNavMenu() {
    }

    public List<CategoryViewModel> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoryViewModel> categories) {
        this.categories = categories;
    }


}
