package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.Reference;

import java.util.Collection;
import java.util.List;

public interface CategoryService {

    /**
     * Gets a list containing all categories that share a parent
     * with at least one of the given categories.
     * @param categoryRefs References to the categories for which the sibling categories should be fetched
     * @return a list of sibling categories
     */
    public List<Category> getSiblingCategories(final Collection<Reference<Category>> categoryRefs);

    /**
     * Gets a list containing the given category itself, and all its ancestors.
     * @param categoryRef A Reference to the category to fetch the breadcrumb categories for
     * @return  the list of breadcrumb categories
     */
    public List<Category> getBreadCrumbCategories(final Reference<Category> categoryRef);
}
