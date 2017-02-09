package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestContext;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class FormUtils {

    private FormUtils() {
    }

    @Nullable
    public static String extractFormField(@Nullable final Form<?> form, final String fieldName) {
        return form != null ? form.field(fieldName).value() : null;
    }

    public static List<String> findAllSelectedValues(final WithFormFieldName<?> settings, final RequestContext requestContext) {
        return requestContext.getQueryString().getOrDefault(settings.getFieldName(), emptyList());
    }

    public static <T> T findSelectedValueFromRequest(final FormSettings<T> settings, final RequestContext requestContext) {
        final List<String> selectedValues = findAllSelectedValues(settings, requestContext);
        return selectedValues.stream()
                .findFirst()
                .map(settings::mapToValue)
                .orElseGet(settings::getDefaultValue);
    }

    public static <T extends FormOption> Optional<T> findSelectedValueFromRequest(final FormSettingsWithOptions<T> settings, final RequestContext requestContext) {
        final List<String> selectedValues = findAllSelectedValues(settings, requestContext);
        return settings.getOptions().stream()
                .filter(option -> selectedValues.contains(option.getValue().toString()))
                .findFirst()
                .map(Optional::of)
                .orElseGet(settings::findDefaultOption);
    }
}
