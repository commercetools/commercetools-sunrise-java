package com.commercetools.sunrise.common.forms;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class FormSettingsWithOptionsTest {

    @Test
    public void selectsDefaultOption() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings(
                option(1, false),
                option(2, false),
                option(3, true));
        assertThat(formSettings.findDefaultOption())
                .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(3));
    }

    @Test
    public void selectsFirstDefaultOptionIfMultiple() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings(
                option(1, false),
                option(2, true),
                option(3, true));
        assertThat(formSettings.findDefaultOption())
                .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(2));
    }

    @Test
    public void selectsFirstOptionIfNoneDefault() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings(
                option(1, false),
                option(2, false),
                option(3, false));
        assertThat(formSettings.findDefaultOption())
                .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(1));
    }

    @Test
    public void selectsNoneIfEmpty() throws Exception {
        final TestableFormSettingsWithOptions formSettings = formSettings();
        assertThat(formSettings.findDefaultOption()).isEmpty();
    }

    private static TestableFormSettingsWithOptions formSettings(final TestableFormOption ... options) {
        return new TestableFormSettingsWithOptions("field-name", asList(options));
    }

    private static TestableFormOption option(final int num, final boolean isDefault) {
        return new TestableFormOption("Option " + num, "option-" + num, num, isDefault);
    }

    private static class TestableFormSettingsWithOptions extends FormSettingsWithOptions<TestableFormOption> {

        TestableFormSettingsWithOptions(final String fieldName, final List<TestableFormOption> options) {
            super(fieldName, options);
        }
    }

    private static class TestableFormOption extends FormOption<Integer> {

        TestableFormOption(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
            super(fieldLabel, fieldValue, value, isDefault);
        }
    }
}
