package productcatalog.models;

import io.sphere.sdk.search.SortExpression;

import java.util.List;

public interface SortOption<T> {

    /**
     * Gets the label displayed in the sort option.
     * @return the label displayed in this sort option
     */
    String getLabel();

    /**
     * Gets the value for this sort option.
     * @return the option value
     */
    String getValue();

    /**
     * Whether this sort option is selected or not.
     * @return true if the option is selected, false otherwise
     */
    boolean isSelected();

    /**
     * Gets the sort model associated with this option, representing the attribute path and sorting direction.
     * @return the sort model for this option
     */
    List<SortExpression<T>> getSortExpressions();

    /**
     * Gets a new instance of SortOption with the same attributes as this, but with the given selected.
     * @param selected the new selected
     * @return a new instance with same attributes, but with the given selected
     */
    SortOption<T> withSelected(boolean selected);

}
