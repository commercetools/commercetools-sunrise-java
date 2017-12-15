package com.commercetools.sunrise.core.viewmodels.header;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import com.commercetools.sunrise.models.categories.CategoryViewModel;

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
