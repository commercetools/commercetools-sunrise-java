package io.sphere.sdk.facets;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CategoryHierarchyMapper implements HierarchyMapper<Category> {
    private final List<FacetOption> facetOptions;
    private final CategoryTree categoryTree;
    private final List<Category> parentCategories;
    private final List<Locale> locales;

    private CategoryHierarchyMapper(final List<FacetOption> facetOptions, final CategoryTree subcategoryTree,
                                   final List<Category> parentCategories, final List<Locale> locales) {
        this.facetOptions = facetOptions;
        this.categoryTree = subcategoryTree;
        this.parentCategories = parentCategories;
        this.locales = locales;
    }

    @Override
    public List<FacetOption> build() {
        return parentCategories.stream()
                .map(this::buildFacetOption)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    @Override
    public Optional<FacetOption> buildFacetOption(final Category category) {
        Optional<FacetOption> facetOption = findFacetOption(category);
        final List<Category> children = categoryTree.findChildren(category);
        if (!children.isEmpty()) {
            facetOption = addChildrenToFacetOption(facetOption, children);
        }
        return setNameToFacetOption(facetOption, category, locales);
    }

    public static CategoryHierarchyMapper of(final List<FacetOption> facetOptions, final CategoryTree categoryTree,
                                             final List<Category> rootCategories, final List<Locale> locales) {
        return new CategoryHierarchyMapper(facetOptions, categoryTree, rootCategories, locales);
    }

    private Optional<FacetOption> addChildrenToFacetOption(final Optional<FacetOption> facetOption, final List<Category> children) {
        boolean selected = facetOption.map(FacetOption::isSelected).orElse(false);
        long count = facetOption.map(FacetOption::getCount).orElse(0L);
        List<FacetOption> childrenFacetOption = new ArrayList<>();

        for (final Category child : children) {
            final Optional<FacetOption> childFacetOption = buildFacetOption(child);
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

    private Optional<FacetOption> findFacetOption(final Category category) {
        return facetOptions.stream()
                .filter(facetOption -> facetOption.getValue().equals(category.getId()))
                .findFirst();
    }
}
