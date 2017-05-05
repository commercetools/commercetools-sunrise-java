package com.commercetools.sunrise.framework.viewmodels.forms;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.someQueryString;
import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.testWithHttpContext;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class FormSettingsTest {

    @Test
    public void findsSelectedValueFromForm() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar");
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("-1", "2", "0", "-5"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getSelectedValue(httpContext))
                        .contains(2));
    }

    @Test
    public void findsAllSelectedValuesFromForm() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar");
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("-1", "2", "0", "3"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getAllSelectedValues(httpContext))
                        .containsExactly(2, 3));
    }

    @Test
    public void emptyIfNoneSelected() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar");
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(formSettings.getSelectedValue(httpContext))
                        .isEmpty());
    }

    @Test
    public void emptyIfNoValidValue() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar");
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("x", "y", "z"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getSelectedValue(httpContext))
                        .isEmpty());
    }

    private static class TestableFormSettings extends AbstractFormSettings<Integer> {

        TestableFormSettings(final String fieldName) {
            super(fieldName);
        }

        @Override
        public Optional<Integer> mapFieldValueToValue(final String fieldValue) {
            try {
                return Optional.of(Integer.valueOf(fieldValue));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        @Override
        public boolean isValidValue(final Integer value) {
            return value > 0;
        }
    }
}
