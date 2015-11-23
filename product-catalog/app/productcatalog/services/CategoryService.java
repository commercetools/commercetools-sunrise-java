package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
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
    List<Category> getSiblings(final Collection<Reference<Category>> categoryRefs);

    /**
     * Gets the subtree of the given parent categories.
     * @param parentCategories the list of parent categories to use as a starting point
     * @return the subtree with the subcategories including the parent categories
     */
    CategoryTree getSubtree(final Collection<Category> parentCategories);

    /**
     * Gets the ancestor of the given category that is in root level.
     * @param category the category which to find the root ancestor
     * @return the root ancestor of the category or the same category in case it is a root itself
     */
    Category getRootAncestor(final Category category);
}
