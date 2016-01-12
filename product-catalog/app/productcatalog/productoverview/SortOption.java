package productcatalog.productoverview;

import common.models.SelectableData;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.SortExpression;

import java.util.List;

public class SortOption<T> extends SelectableData {
    private List<SortExpression<T>> expressions;

    public SortOption() {
    }

    public SortOption(final String label, final String value, final List<SortExpression<T>> expressions) {
        super(label, value);
        this.expressions = expressions;
    }

    /**
     * Gets the sort model associated with this option, representing the attribute path and sorting direction.
     * @return the sort model for this option
     */
    public List<SortExpression<T>> getSortExpressions() {
        return expressions;
    }
}
