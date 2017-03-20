package com.commercetools.sunrise.ctp;

import com.google.common.base.CaseFormat;

public final class CtpEnumUtils {

    private CtpEnumUtils() {
    }

    /**
     * Transforms a CTP enum name (e.g. BALANCE_DUE) to lower CamelCase (e.g. balanceDue).
     * @param ctpEnumName the CTP enum name to transform into lower CamelCase
     * @return the lower CamelCase version of the CTP enum name
     */
    public static String enumToCamelCase(final String ctpEnumName) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, ctpEnumName);
    }
}
