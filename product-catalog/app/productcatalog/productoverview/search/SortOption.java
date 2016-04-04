package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SortOption extends Base {
    private String value;
    private String label;
    private List<SortExpression<ProductProjection>> expressions;

    public SortOption(final String value, final String label, final List<SortExpression<ProductProjection>> expressions) {
        this.label = label;
        this.value = value;
        this.expressions = expressions;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Gets the sort model associated with this option, representing the attribute path and sorting direction.
     * @return the sort model for this option
     */
    public List<SortExpression<ProductProjection>> getExpressions() {
        return expressions;
    }

    public static SortOption of(final String value, final String label, final List<String> expressions) {
        final List<SortExpression<ProductProjection>> sortExpressions = expressions.stream()
                .map(SortOption::toSortExpression)
                .collect(toList());
        return new SortOption(value, label, sortExpressions);
    }

    private static SortExpression<ProductProjection> toSortExpression(final String expr) {
        return SortExpression.of(expr);
    }
}
