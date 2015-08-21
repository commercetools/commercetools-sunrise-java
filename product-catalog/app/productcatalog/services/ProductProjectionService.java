package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import play.libs.F;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface ProductProjectionService {

    /**
     * Gets a list of Products from a PagedQueryResult
     * @param page the page
     * @param pageSize number of products per page
     * @return A Promise of the list of ProductProjections
     */
    F.Promise<List<ProductProjection>> searchProducts(final int page, final int pageSize);

    /**
     * Gets a product, uniquely identified by a locale and a slug
     * @param locale the locale in which you provide the slug
     * @param slug the slug
     * @return A Promise of an optionally found ProductProjection
     */
    F.Promise<Optional<ProductProjection>> searchProductBySlug(final Locale locale, final String slug);

    /**
     * Gets a List of length numSuggestions of Products from the given categories
     * @param categories a list of categories to get the products out of
     * @param numSuggestions the number of products the returned list should contain.
     *                       It might contain less if the requested number is greater
     *                       than the number of available products.
     * @return A Promise of the list of products without duplicates
     */
    F.Promise<List<ProductProjection>> getSuggestions(final List<Category> categories, final int numSuggestions);
}
