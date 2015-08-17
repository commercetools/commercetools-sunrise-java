package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public interface CategoryService {

    /**
     * Gets a list containing all categories that share a parent
     * with at least one of the given products categories.
     * @param product the product for which the sibling categories should be fetched
     * @return a list of sibling categories
     */
    public List<Category> getSiblingCategories(final ProductProjection product);

    /**
     * Gets a list containing the main category of the product itself, and all its ancestors.
     * @param product the product to fetch the breadcrumb categories for
     * @return  the list of breadcrumb categories
     */
    public List<Category> getBreadCrumbCategories(final ProductProjection product);
}
