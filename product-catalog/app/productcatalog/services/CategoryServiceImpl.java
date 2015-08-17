package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryTree categories;

    @Inject
    public CategoryServiceImpl(final CategoryTree categories) {
        this.categories = categories;
    }

    public List<Category> getSiblingCategories(final Collection<Reference<Category>> categoryRefs) {
        return categoryRefs.stream()
            .map(this::expandCategory).filter(Optional::isPresent).map(Optional::get)
            .map(Category::getParent).filter(Optional::isPresent).map(Optional::get)
            .flatMap(parent -> categories.findByParent(parent).stream())
            .distinct()
            .collect(toList());
    }

    public List<Category> getBreadCrumbCategories(final Reference<Category> categoryRef) {
        final Optional<Category> categoryOptional = expandCategory(categoryRef);
        return categoryOptional.map(this::getCategoryWithAncestors).orElse(emptyList());
    }

    private List<Category> getCategoryWithAncestors(final Category category) {
        return concat(category.getAncestors().stream().map(this::expandCategory), Stream.of(Optional.of(category)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<Category> expandCategory(final Reference<Category> categoryRef) {
        return categories.findById(categoryRef.getId());
    }
}
