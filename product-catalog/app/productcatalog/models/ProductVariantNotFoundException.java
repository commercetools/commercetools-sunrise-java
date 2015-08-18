package productcatalog.models;

public class ProductVariantNotFoundException extends RuntimeException {
    public ProductVariantNotFoundException(final String message) {
        super(message);
    }

    public static ProductVariantNotFoundException bySku(final String sku) {
        return new ProductVariantNotFoundException("ProductVariant not found for sku " + sku);
    }
}
