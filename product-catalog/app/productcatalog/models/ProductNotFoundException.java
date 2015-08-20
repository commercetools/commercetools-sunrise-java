package productcatalog.models;

import java.util.Locale;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(final String message) {
        super(message);
    }

    public static ProductNotFoundException bySlug(final Locale locale, final String slug) {
        return new ProductNotFoundException("Product not found for slug " + locale + " " + slug);
    }
}
