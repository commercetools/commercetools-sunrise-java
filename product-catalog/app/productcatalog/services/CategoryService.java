package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    /**
     * Gets a list containing all categories that share a parent
     * with at least one of the given categories.
     * @param categoryRefs References to the categories for which the sibling categories should be fetched
     * @return a list of sibling categories
     */
    List<Category> getSiblingCategories(final Collection<Reference<Category>> categoryRefs);

    /**
     * Gets a list containing the given category itself, and all its ancestors.
     * @param categoryRef A Reference to the category to fetch the breadcrumb categories for
     * @return  the list of breadcrumb categories
     */
    List<Category> getBreadCrumbCategories(final Reference<Category> categoryRef);

    /**
     * Gets the 'New' category, if any.
     * @return the 'New' category, or absent if it does not exists
     */
    Optional<Category> getNewCategory();

    /**
     * Finds out whether a category is a successor of the 'New' category
     * @param category the category that might be a successor of 'New'
     * @return if the category is a successor
     */
    boolean categoryIsInNew(final Reference<Category> category);

    /**
     * Gets a subtree from the given CategoryTree with the given Category as root.
     * If the given Category is not in the given CategoryTree the returned CategoryTree will be empty.
     * @param category
     * @return The Subtree starting at the given Category
     */
    CategoryTree getSubtree(final CategoryTree categoryTree, final Category category);
}
