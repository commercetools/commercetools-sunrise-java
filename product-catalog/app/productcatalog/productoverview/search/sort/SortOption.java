package productcatalog.productoverview.search.sort;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;

public final class SortOption extends Base {

    private String value;
    private String label;
    private List<SortExpression<ProductProjection>> expressions;
    private boolean isDefault;

    private SortOption(final String value, final String label, final List<SortExpression<ProductProjection>> expressions,
                         final boolean isDefault) {
        this.label = label;
        this.value = value;
        this.expressions = expressions;
        this.isDefault = isDefault;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Gets the sort model associated with this option, representing the attribute path and sorting direction.
     * @return the sort model for this option
     */
    public List<SortExpression<ProductProjection>> getExpressions() {
        return expressions;
    }

    public SortOption withExpressions(final List<SortExpression<ProductProjection>> expressions) {
        return new SortOption(value, label, expressions, isDefault);
    }

    public static SortOption of(final String value, final String label, final List<SortExpression<ProductProjection>> sortExpressions,
                                final boolean isDefault) {
        return new SortOption(value, label, sortExpressions, isDefault);
    }
}
