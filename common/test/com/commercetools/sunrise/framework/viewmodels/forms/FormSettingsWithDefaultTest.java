package com.commercetools.sunrise.framework.viewmodels.forms;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.someQueryString;
import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.testWithHttpContext;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class FormSettingsWithDefaultTest {

    @Test
    public void findsSelectedValueFromForm() throws Exception {
        final TestableFormSettingsWithDefault formSettings = new TestableFormSettingsWithDefault("bar", 10);
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("-1", "2", "0", "-5"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getSelectedValueOrDefault(httpContext))
                        .isEqualTo(2));
    }

    @Test
    public void fallbacksToDefaultValueIfNoneSelected() throws Exception {
        final TestableFormSettingsWithDefault formSettings = new TestableFormSettingsWithDefault("bar", 10);
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(formSettings.getSelectedValueOrDefault(httpContext))
                        .isEqualTo(10));
    }

    @Test
    public void fallbacksToDefaultValueIfNoValidValue() throws Exception {
        final TestableFormSettingsWithDefault formSettings = new TestableFormSettingsWithDefault("bar", 10);
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("x", "y", "z"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getSelectedValueOrDefault(httpContext))
                        .isEqualTo(10));
    }

    private static class TestableFormSettingsWithDefault extends AbstractFormSettingsWithDefault<Integer> {

        TestableFormSettingsWithDefault(final String fieldName, final Integer defaultValue) {
            super(fieldName, defaultValue);
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
