package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.PagedResult;
import play.libs.F;

import javax.inject.Inject;
import java.util.*;

public interface ProductProjectionService {

    /**
     * Gets a list of Products from a PagedQueryResult
     * @param page the page
     * @param pageSize number of products per page
     * @return A Promise of the list of ProductProjections
     */
    public F.Promise<List<ProductProjection>> searchProducts(final int page, final int pageSize);

    /**
     * Gets a product, uniquely identified by a locale and a slug
     * @param locale the locale in which you provide the slug
     * @param slug the slug
     * @return A Promise of an optionally found ProductProjection
     */
    public F.Promise<Optional<ProductProjection>> searchProductBySlug(final Locale locale, final String slug);

    /**
     * Finds a variant of a given product by sku or None if nothing was found
     * @param product the product to find the variant from
     * @param sku the sku of the variant
     * @return A Promise of an optionally founf variant
     */
    public Optional<ProductVariant> findVariantBySku(final ProductProjection product, final String sku);

    /**
     * Gets a List of length numSuggestions of Products from the given categories
     * @param categories a list of categories to get the products out of
     * @param numSuggestions the number of products the returned list should contain.
     *                       It might contain less if the requested number is greater
     *                       than the number of available products.
     * @return A Promise of the list of products without duplicates
     */
    public F.Promise<List<ProductProjection>> getSuggestions(final List<Category> categories, final int numSuggestions);
}
