package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;
import play.Configuration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class CategoryServiceImpl implements CategoryService {
    private static final String CONFIG_NEW_CATEGORY_EXTERNAL_ID = "common.newCategoryExternalId";
    private final Optional<CategoryTree> newSubcategoryTree;
    private final Optional<Category> newCategory;
    private final CategoryTree categories;

    @Inject
    public CategoryServiceImpl(final CategoryTree categories, final Configuration configuration) {
        this.categories = categories;
        final String newExternalId = configuration.getString(CONFIG_NEW_CATEGORY_EXTERNAL_ID);
        this.newCategory = categories.findByExternalId(newExternalId);
        this.newSubcategoryTree = newCategory.map(c -> getSubtree(categories, c));
    }

    @Override
    public List<Category> getSiblingCategories(final Collection<Reference<Category>> categoryRefs) {
        return categoryRefs.stream()
                .map(this::expandCategory).filter(Optional::isPresent).map(Optional::get)
                .map(category -> Optional.ofNullable(category.getParent())).filter(Optional::isPresent).map(Optional::get)
                .flatMap(parent -> categories.findChildren(parent).stream())
                .distinct()
                .collect(toList());
    }

    @Override
    public Optional<Category> getNewCategory() {
        return newCategory;
    }

    @Override
    public boolean categoryIsInNew(Reference<Category> category) {
        return newSubcategoryTree.map(subtree -> subtree.findById(category.getId()).isPresent()).orElse(false);
    }

    @Override
    public CategoryTree getSubtree(final CategoryTree categoryTree, final Category category) {
        final List<Category> subTreeCategories = new ArrayList<>();
        addSubtreeCategories(categoryTree, subTreeCategories, category);
        return CategoryTree.of(subTreeCategories);
    }


    private Optional<Category> expandCategory(final Reference<Category> categoryRef) {
        return categories.findById(categoryRef.getId());
    }

    private void addSubtreeCategories(final CategoryTree categoryTree, final List<Category> acc, final Category category) {
        acc.add(category);
        categoryTree.findChildren(category).stream().forEach(child -> addSubtreeCategories(categoryTree, acc, child));
    }
}
