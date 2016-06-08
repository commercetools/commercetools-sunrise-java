package productcatalog.productoverview.search.productsperpage;

import io.sphere.sdk.models.Base;

public final class ProductsPerPageOption extends Base {

    private String value;
    private String label;
    private int amount;

    private ProductsPerPageOption(final String value, final String label, final int amount) {
        this.label = label;
        this.value = value;
        this.amount = amount;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getAmount() {
        return amount;
    }

    public static ProductsPerPageOption of(final String value, final String label, final int amount) {
        return new ProductsPerPageOption(value, label, amount);
    }
}
