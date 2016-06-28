package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;
import play.data.Form;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static java.util.Collections.singletonList;

public class UserFeedback extends Base {

    public static String ERROR = "user-error";
    public static String WARNING = "user-warning";
    public static String MESSAGE = "user-message";

    @Inject
    private Http.Context httpContext;

    public Optional<ErrorsBean> getErrors() {
        return Optional.ofNullable(httpContext.args.get(ERROR))
                .flatMap(errorsObj -> {
                    if (errorsObj instanceof ErrorsBean) {
                        return Optional.of((ErrorsBean) errorsObj);
                    } else {
                        return Optional.empty();
                    }
                });
    }

    public void addErrors(final String message) {
        addErrors(singletonList(new ErrorBean(message)));
    }

    public void addErrors(@Nullable final Form<?> form) {
        addErrors(extractErrors(form));
    }

    private void addErrors(final List<ErrorBean> errors) {
        final LinkedList<ErrorBean> errorBeans = new LinkedList<>(errors);
        if (!errorBeans.isEmpty()) {
            Optional.ofNullable(httpContext.args.get(ERROR))
                    .ifPresent(errorsObj -> {
                        if (errorsObj instanceof ErrorsBean) {
                            errorBeans.addAll(((ErrorsBean) errorsObj).getGlobalErrors());
                        }
                    });
            replaceErrorsFromContext(errorBeans);
        }
    }

    private void replaceErrorsFromContext(final List<ErrorBean> errorList) {
        final ErrorsBean errors = new ErrorsBean();
        errors.setGlobalErrors(errorList);
        httpContext.args.put(ERROR, errors);
    }

    private List<ErrorBean> extractErrors(final @Nullable Form<?> form) {
        final List<ErrorBean> errorList = new ArrayList<>();
        if (form != null) {
            form.errors().forEach((field, errors) ->
                    errors.forEach(error -> errorList.add(new ErrorBean(error.key() + ": " + error.message()))));
        }
        return errorList;
    }
}
