package io.sphere.sdk.facets;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Mapper that transforms facet options with Category IDs into a hierarchical list of facet options, as defined by the given category list.
 * The IDs are then replaced by the Category name, in a language according to the provided locales.
 * Any facet option that is not represented in the list of categories or doesn't contain a name for the locales, is discarded.
 */
public class HierarchicalCategoryFacetOptionMapper implements FacetOptionMapper {
    private final CategoryTree subcategoryTree;
    private final List<Locale> locales;

    private HierarchicalCategoryFacetOptionMapper(final CategoryTree subcategoryTree, final List<Locale> locales) {
        this.subcategoryTree = subcategoryTree;
        this.locales = locales;
    }

    @Override
    public List<FacetOption> apply(final List<FacetOption> facetOptions) {
        return getRootCategories().stream()
                .map(root -> buildFacetOption(root, facetOptionFinder(facetOptions)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public static HierarchicalCategoryFacetOptionMapper of(final List<Category> subcategories, final List<Locale> locales) {
        final CategoryTree subcategoryTree = CategoryTree.of(subcategories);
        return new HierarchicalCategoryFacetOptionMapper(subcategoryTree, locales);
    }

    private List<Category> getRootCategories() {
        return subcategoryTree.getAllAsFlatList().stream().filter(category -> {
            final Optional<Reference<Category>> parentRef = Optional.ofNullable(category.getParent());
            return parentRef.map(parent -> !subcategoryTree.findById(parent.getId()).isPresent()).orElse(true);
        }).collect(toList());
    }

    private Optional<FacetOption> buildFacetOption(final Category category, final Function<Category, Optional<FacetOption>> facetOptionFinder) {
        Optional<FacetOption> facetOption = facetOptionFinder.apply(category);
        final List<Category> children = subcategoryTree.findChildren(category);
        if (!children.isEmpty()) {
            facetOption = addChildrenToFacetOption(facetOption, children, facetOptionFinder);
        }
        return setNameToFacetOption(facetOption, category, locales);
    }

    private Optional<FacetOption> addChildrenToFacetOption(final Optional<FacetOption> facetOption, final List<Category> children,
                                                           final Function<Category, Optional<FacetOption>> facetOptionFinder) {
        boolean selected = facetOption.map(FacetOption::isSelected).orElse(false);
        long count = facetOption.map(FacetOption::getCount).orElse(0L);
        List<FacetOption> childrenFacetOption = new ArrayList<>();

        for (final Category child : children) {
            final Optional<FacetOption> childFacetOption = buildFacetOption(child, facetOptionFinder);
            if (childFacetOption.isPresent()) {
                selected |= childFacetOption.get().isSelected();
                count += childFacetOption.get().getCount();
                childrenFacetOption.add(childFacetOption.get());
            }
        }
        return updateFacetOption(facetOption, selected, count, childrenFacetOption);
    }

    private Optional<FacetOption> updateFacetOption(final Optional<FacetOption> facetOption, final boolean selected,
                                                    final long count, final List<FacetOption> childrenFacetOption) {
        FacetOption updatedFacetOption = null;
        if (facetOption.isPresent()) {
            updatedFacetOption = facetOption.get()
                    .withCount(count)
                    .withSelected(selected)
                    .withChildren(childrenFacetOption);
        } else if (!childrenFacetOption.isEmpty()) {
            updatedFacetOption = FacetOption.of("", count, selected).withChildren(childrenFacetOption);
        }
        return Optional.ofNullable(updatedFacetOption);
    }

    private Optional<FacetOption> setNameToFacetOption(final Optional<FacetOption> facetOptionOptional,
                                                       final Category category, final List<Locale> locales) {
        return facetOptionOptional.flatMap(facetOption -> {
            final String name = category.getName().get(locales);
            return Optional.ofNullable(name).map(facetOption::withValue);
        });
    }

    private Function<Category, Optional<FacetOption>> facetOptionFinder(final List<FacetOption> facetOptions) {
        return category -> facetOptions.stream()
                .filter(facetOption -> facetOption.getValue().equals(category.getId()))
                .findFirst();
    }
}
