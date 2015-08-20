package io.sphere.sdk.facets;

import java.util.List;
import java.util.Optional;

public interface SelectFacet extends Facet {

    /**
     * Gets the complete options without limitations.
     * @return the complete possible options
     */
    List<FacetOption> getAllOptions();

    /**
     * Obtains the truncated options list according to the defined limit.
     * @return the truncated options if the limit is defined, the whole list otherwise
     */
    List<FacetOption> getLimitedOptions();

    /**
     * Gets the threshold indicating the minimum amount of options allowed to be displayed in the facet.
     * @return the threshold for the amount of options that can be displayed, or absent if it has no threshold
     */
    Optional<Long> getThreshold();

    /**
     * Gets the limit for the maximum amount of options allowed to be displayed in the facet.
     * @return the limit for the amount of options that can be displayed, or absent if it has no limit
     */
    Optional<Long> getLimit();

}
