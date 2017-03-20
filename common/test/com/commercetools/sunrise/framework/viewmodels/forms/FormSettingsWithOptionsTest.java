package com.commercetools.sunrise.framework.viewmodels.forms;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.someQueryString;
import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.testWithHttpContext;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class FormSettingsWithOptionsTest {

    @Test
    public void selectsDefaultOption() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(2, false),
                option(3, true));
        assertThat(formSettings.findDefaultOption())
                .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(3));
    }

    @Test
    public void selectsFirstDefaultOptionIfMultiple() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(2, true),
                option(3, true));
        assertThat(formSettings.findDefaultOption())
                .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(2));
    }

    @Test
    public void selectsFirstOptionIfNoneDefault() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(2, false),
                option(3, false));
        assertThat(formSettings.findDefaultOption())
                .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(1));
    }

    @Test
    public void selectsNoneIfEmpty() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar");
        assertThat(formSettings.findDefaultOption()).isEmpty();
    }

    @Test
    public void findsSelectedValueFromFormWithOptions() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(10, true),
                option(50, false));
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "3", "4"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getSelectedOption(httpContext))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(1)));
    }

    @Test
    public void findsAllSelectedValuesFromFormWithOptions() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(10, true),
                option(50, false));
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "10", "4"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getAllSelectedOptions(httpContext))
                        .extracting(FormOption::getValue)
                        .containsExactly(1, 10));
    }

    @Test
    public void selectsFirstOptionIfNoDefaultOptionAndNoneSelected() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(10, false),
                option(50, false));
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(formSettings.getSelectedOption(httpContext))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(1)));
    }

    @Test
    public void emptyIfNoOptionsProvided() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar");
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(formSettings.getSelectedOption(httpContext))
                        .isEmpty());
    }

    @Test
    public void fallbacksToDefaultOptionIfNoValidValue() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(10, true),
                option(50, false));
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("x", "y", "z"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(formSettings.getSelectedOption(httpContext))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(10)));
    }

    @Test
    public void fallbacksToDefaultOptionIfNoneSelected() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings("bar",
                option(1, false),
                option(10, true),
                option(50, false));
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(formSettings.getSelectedOption(httpContext))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(10)));
    }

    private static TestableFormSettingsWithOptions formSettings(final String fieldName, final TestableFormOption ... options) {
        return new TestableFormSettingsWithOptions(fieldName, asList(options));
    }

    private static TestableFormOption option(final int num, final boolean isDefault) {
        return new TestableFormOption("Option " + num, String.valueOf(num), num, isDefault);
    }

    private static class TestableFormSettingsWithOptions extends AbstractFormSettingsWithOptions<TestableFormOption, Integer> {

        TestableFormSettingsWithOptions(final String fieldName, final List<TestableFormOption> options) {
            super(fieldName, options);
        }
    }

    private static class TestableFormOption extends AbstractFormOption<Integer> {

        TestableFormOption(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
            super(fieldLabel, fieldValue, value, isDefault);
        }
    }
}
