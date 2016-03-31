package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.SortExpression;

import java.util.List;

public class SortOption<T> extends Base {
    private String label;
    private String value;
    private List<SortExpression<T>> expressions;

    public SortOption() {
    }

    public SortOption(final String label, final String value, final List<SortExpression<T>> expressions) {
        this.label = label;
        this.value = value;
        this.expressions = expressions;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    /**
     * Gets the sort model associated with this option, representing the attribute path and sorting direction.
     * @return the sort model for this option
     */
    public List<SortExpression<T>> getExpressions() {
        return expressions;
    }
}
