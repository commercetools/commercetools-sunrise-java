package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class CategoryService {

    private final CategoryTree categories;

    @Inject
    public CategoryService(final CategoryTree categories) {
        this.categories = categories;
    }

    public List<Category> getSiblingCategories(final ProductProjection product) {
        return product.getCategories().stream().findFirst()
                .flatMap(this::expandCategory)
                .flatMap(category -> category.getParent().map(categories::findByParent))
                .orElse(emptyList());
    }

    public List<Category> getBreadCrumbCategories(final ProductProjection product) {
        final Optional<Category> categoryOptional = product.getCategories().stream().findFirst()
                .flatMap(this::expandCategory);

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
