package productcatalog.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.SortExpression;

import java.util.List;

public class SortOptionImpl<T> extends Base implements SortOption<T> {
    private final String value;
    private final String label;
    private final boolean selected;
    private final List<SortExpression<T>> expressions;

    SortOptionImpl(final String value, final String label, final boolean selected, final List<SortExpression<T>> expressions) {
        this.value = value;
        this.label = label;
        this.selected = selected;
        this.expressions = expressions;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public List<SortExpression<T>> getSortExpressions() {
        return expressions;
    }

    @Override
    public SortOption<T> withSelected(final boolean selected) {
        return new SortOptionImpl<>(value, label, selected, expressions);
    }

    public static <T> SortOption<T> of(final String value, final String label, final boolean selected, final List<SortExpression<T>> searchExpressions) {
        return new SortOptionImpl<>(value, label, selected, searchExpressions);
    }
}
