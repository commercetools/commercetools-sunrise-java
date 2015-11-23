package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;

import javax.inject.Inject;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryTree categoryTree;

    @Inject
    public CategoryServiceImpl(final CategoryTree categoryTree) {
        this.categoryTree = categoryTree;
    }

    @Override
    public List<Category> getSiblings(final Collection<Reference<Category>> categoryRefs) {
        return categoryRefs.stream()
                .map(this::refToCategory)
                .filter(Optional::isPresent)
                .flatMap(category -> getSiblings(category.get()).stream())
                .distinct()
                .collect(toList());
    }

    @Override
    public CategoryTree getSubtree(final Collection<Category> parentCategories) {
        return CategoryTree.of(getSubtreeAsFlatList(parentCategories));
    }

    public Category getRootAncestor(final Category category) {
        return category.getAncestors().stream().findFirst()
                .flatMap(root -> categoryTree.findById(root.getId()))
                .orElse(category);
    }

    private List<Category> getSiblings(final Category category) {
        return Optional.ofNullable(category.getParent())
                .map(categoryTree::findChildren)
                .orElse(emptyList());
    }

    private List<Category> getSubtreeAsFlatList(final Collection<Category> parentCategories) {
        final List<Category> categories = new ArrayList<>();
        parentCategories.stream().forEach(parent -> {
            categories.add(parent);
            final List<Category> children = categoryTree.findChildren(parent);
            categories.addAll(getSubtreeAsFlatList(children));
        });
        return categories;
    }

    private Optional<Category> refToCategory(final Reference<Category> ref) {
        return categoryTree.findById(ref.getId());
    }
}
