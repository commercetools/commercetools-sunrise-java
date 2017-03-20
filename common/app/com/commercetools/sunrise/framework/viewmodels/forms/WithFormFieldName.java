package com.commercetools.sunrise.framework.viewmodels.forms;

import play.mvc.Http;

import java.util.List;

import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.findAllSelectedValuesFromQueryString;

public interface WithFormFieldName {

    String getFieldName();

    default List<String> getSelectedValuesAsRawList(final Http.Context httpContext) {
        return findAllSelectedValuesFromQueryString(getFieldName(), httpContext.request());
    }
}

