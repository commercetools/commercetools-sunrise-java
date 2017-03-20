package com.commercetools.sunrise.framework.viewmodels.header;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.categories.CategoryViewModel;

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
