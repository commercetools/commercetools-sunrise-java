package io.sphere.sdk.categories;

import io.sphere.sdk.models.Identifiable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface CategoryTreeExtended extends CategoryTree {

    @Override
    List<Category> getRoots();

    @Override
    Optional<Category> findById(String id);

    @Override
    Optional<Category> findByExternalId(String externalId);

    @Override
    Optional<Category> findBySlug(Locale locale, String slug);

    @Override
    List<Category> getAllAsFlatList();

    @Override
    List<Category> findChildren(Identifiable<Category> category);

    /**
     * Gets a list containing all categories that share a parent
     * with at least one of the given categories.
     * @param categoryIds Categories for which the sibling categories should be fetched
     * @return a list of sibling categories
     */
    List<Category> getSiblings(final Collection<Category> categoryIds);


    /**
     * Gets the ancestor of the given category that is in root level.
     * @param category the category which to find the root ancestor
     * @return the root ancestor of the category or the same category in case it is a root itself
     */
    Category getRootAncestor(final Category category);

    /**
     * Creates a category tree from a flat list of categories.
     *
     * @param allCategoriesAsFlatList all categories as flat list.
     * @return the created category tree.
     */
    static CategoryTreeExtended of(final List<Category> allCategoriesAsFlatList) {
        requireNonNull(allCategoriesAsFlatList);
        return new CategoryTreeExtendedImpl(allCategoriesAsFlatList);
    }
}
