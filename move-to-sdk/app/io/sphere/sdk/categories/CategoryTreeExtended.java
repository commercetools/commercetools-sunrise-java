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

    @Override
    List<Category> findSiblings(Collection<? extends Identifiable<Category>> collection);

    @Override
    Category getRootAncestor(Identifiable<Category> identifiable);

    @Override
    CategoryTree getSubtree(Collection<? extends Identifiable<Category>> collection);

    @Override
    List<Category> getSubtreeRoots();

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
