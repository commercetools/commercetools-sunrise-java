package com.commercetools.sunrise.common.categorytree;

import io.sphere.sdk.categories.Category;
import org.apache.commons.lang3.ObjectUtils;

public final class ByOrderHintCategoryComparator implements CategoryComparator {

    @Override
    public int compare(final Category c1, final Category c2) {
        return ObjectUtils.compare(c1.getOrderHint(), c2.getOrderHint());
    }
}
