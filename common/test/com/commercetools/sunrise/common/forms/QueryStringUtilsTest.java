package com.commercetools.sunrise.common.forms;

import org.junit.Test;
import play.mvc.Http;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.commercetools.sunrise.common.forms.QueryStringUtils.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;

public class QueryStringUtilsTest {

    @Test
    public void findsAllSelectedValues() throws Exception {
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "3", "4"));
        testWithHttpRequest(queryString, httpRequest ->
                assertThat(findAllSelectedValuesFromQueryString("bar", httpRequest))
                        .containsExactly("1", "2", "3", "4"));
    }

    @Test
    public void findsOneSelectedValue() throws Exception {
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "3", "4"));
        testWithHttpRequest(queryString, httpRequest ->
                assertThat(findSelectedValueFromQueryString("bar", httpRequest))
                        .hasValueSatisfying(value -> assertThat(value).isIn("1", "2", "3", "4")));
    }

    @Test
    public void findsNoneIfValueNotPresent() throws Exception {
        testWithHttpRequest(someQueryString(), httpRequest ->
                assertThat(findAllSelectedValuesFromQueryString("bar", httpRequest))
                        .isEmpty());
        testWithHttpRequest(someQueryString(), httpRequest ->
                assertThat(findSelectedValueFromQueryString("bar", httpRequest))
                        .isEmpty());
    }

    @Test
    public void findsNoneIfEmptyQueryString() throws Exception {
        testWithHttpRequest(emptyMap(), httpRequest ->
                assertThat(findAllSelectedValuesFromQueryString("bar", httpRequest))
                        .isEmpty());
        testWithHttpRequest(emptyMap(), httpRequest ->
                assertThat(findSelectedValueFromQueryString("bar", httpRequest))
                        .isEmpty());
    }

    @Test
    public void findsSelectedValueFromForm() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar", 10);
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("-1", "2", "0", "-5"));
        testWithHttpRequest(queryString, httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .isEqualTo(2));
    }

    @Test
    public void findsSelectedValueFromFormWithOptions() throws Exception {
        final TestableFormSettingsWithOptions formSettings = new TestableFormSettingsWithOptions("bar", asList(
                option(1, false),
                option(10, true),
                option(50, false)));
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "3", "4"));
        testWithHttpRequest(queryString, httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(1)));
    }

    @Test
    public void fallbacksToDefaultValueIfNoneSelected() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar", 10);
        testWithHttpRequest(someQueryString(), httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .isEqualTo(10));
    }

    @Test
    public void fallbacksToDefaultOptionIfNoneSelected() throws Exception {
        final TestableFormSettingsWithOptions formSettings = new TestableFormSettingsWithOptions("bar", asList(
                option(1, false),
                option(10, true),
                option(50, false)));
        testWithHttpRequest(someQueryString(), httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(10)));
    }

    @Test
    public void fallbacksToDefaultValueIfNoValidValue() throws Exception {
        final TestableFormSettings formSettings = new TestableFormSettings("bar", 10);
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("x", "y", "z"));
        testWithHttpRequest(queryString, httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .isEqualTo(10));
    }

    @Test
    public void fallbacksToDefaultOptionIfNoValidValue() throws Exception {
        final TestableFormSettingsWithOptions formSettings = new TestableFormSettingsWithOptions("bar", asList(
                option(1, false),
                option(10, true),
                option(50, false)));
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("x", "y", "z"));
        testWithHttpRequest(queryString, httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(10)));
    }

    @Test
    public void selectsFirstOptionIfNoDefaultOptionAndNoneSelected() throws Exception {
        final TestableFormSettingsWithOptions formSettings = new TestableFormSettingsWithOptions("bar", asList(
                option(1, false),
                option(10, false),
                option(50, false)));
        testWithHttpRequest(someQueryString(), httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                    .hasValueSatisfying(option -> assertThat(option.getValue()).isEqualTo(1)));
    }

    @Test
    public void emptyIfNoOptionsProvided() throws Exception {
        final TestableFormSettingsWithOptions formSettings = new TestableFormSettingsWithOptions("bar", emptyList());
        testWithHttpRequest(someQueryString(), httpRequest ->
                assertThat(findSelectedValueFromQueryString(formSettings, httpRequest))
                        .isEmpty());
    }

    @Test
    public void buildsUri() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("jacket", "shirt", "shoes"));
        queryString.put("bar", asList("1", "2"));
        queryString.put("qux", singletonList("x"));
        queryString.put("baz", emptyList());
        final String expectedUri = "some-path?bar=1&bar=2&qux=x&foo=jacket&foo=shirt&foo=shoes";
        final String uri = buildUri("some-path", queryString);
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI stays the same")
                .isEqualTo(expectedUri);
    }

    @Test
    public void builtUrlShouldHaveQueryStringValuesEncoded() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("jacket shirt", "shoes "));
        queryString.put("bar", asList("1 2", "€√!L", "*-._~"));
        final String expectedUri = "some-path?bar=1+2&bar=%E2%82%AC%E2%88%9A%21L&bar=*-._%7E&foo=jacket+shirt&foo=shoes+";
        final String uri = buildUri("some-path", queryString);
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI matches too")
                .isEqualTo("some-path?bar=1 2&bar=€√!L&bar=*-._~&foo=jacket shirt&foo=shoes ");
    }

    @Test
    public void extractsQueryString() throws Exception {
        final Map<String, List<String>> queryString = someQueryString();
        testWithHttpRequest(queryString, request ->
                assertThat(extractQueryString(request))
                        .isEqualTo(queryString));
    }

    private static Map<String, List<String>> someQueryString() {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("x", "y", "z"));
        queryString.put("qux", asList("v", "w"));
        return queryString;
    }

    private static void testWithHttpRequest(final Map<String, List<String>> queryString, final Consumer<Http.Request> test) {
        final Http.Request request = fakeRequest()
                .uri(QueryStringUtils.buildUri("path", queryString))
                .build();
        test.accept(request);
    }

    private static TestableFormSettingsWithOptions formSettings(final TestableFormOption... options) {
        return new TestableFormSettingsWithOptions("field-name", asList(options));
    }

    private static TestableFormOption option(final int num, final boolean isDefault) {
        return new TestableFormOption("Option " + num, String.valueOf(num), num, isDefault);
    }

    private static class TestableFormSettings extends FormSettings<Integer> {

        TestableFormSettings(final String fieldName, final Integer defaultValue) {
            super(fieldName, defaultValue);
        }

        @Override
        public Integer mapToValue(final String valueAsString) {
            try {
                return Integer.valueOf(valueAsString);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        @Override
        public boolean isValidValue(final Integer value) {
            return value > 0;
        }
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
