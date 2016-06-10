package common.utils;

import com.commercetools.sunrise.common.configuration.SunriseConfiguration;
import org.junit.Test;
import play.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseConfigurationTest {
    private static final String CONFIG_KEY = "key";

    @Test
    public void ignoresEmptyValues() throws Exception {
        testWithConfig(CONFIG_KEY, "foo, ,baz", config ->
                assertThat(config.getStringList(CONFIG_KEY)).containsExactly("foo", "baz"));
    }

    @Test
    public void returnsNullWithEmptyString() throws Exception {
        testWithConfig(CONFIG_KEY, " ", config ->
                assertThat(config.getStringList(CONFIG_KEY)).isNull());
    }

    @Test
    public void returnsNullWithJustComma() throws Exception {
        testWithConfig(CONFIG_KEY, ",", config ->
                assertThat(config.getStringList(CONFIG_KEY)).isNull());
    }

    @Test
    public void parsesStringList() throws Exception {
        testWithConfig(CONFIG_KEY, "foo, bar, baz", config ->
                assertThat(config.getStringList(CONFIG_KEY)).containsExactly("foo", "bar", "baz"));
    }

    @Test
    public void parsesStringListWithDefault() throws Exception {
        final List<String> defaultList = asList("foo", "qux");
        testWithConfig(CONFIG_KEY, "foo, bar, baz", config ->
                assertThat(config.getStringList(CONFIG_KEY, defaultList)).containsExactly("foo", "bar", "baz"));
    }

    @Test
    public void returnsDefaultStringListWithEmptyString() throws Exception {
        final List<String> defaultList = asList("foo", "qux");
        testWithConfig(CONFIG_KEY, "", config ->
                assertThat(config.getStringList(CONFIG_KEY, defaultList)).containsExactlyElementsOf(defaultList));
    }

    @Test
    public void parsesBooleanList() throws Exception {
        testWithConfig(CONFIG_KEY, "true, false, baz", config ->
                assertThat(config.getBooleanList(CONFIG_KEY)).containsExactly(true, false, false));
    }

    @Test
    public void parsesBooleanListWithDefault() throws Exception {
        final List<Boolean> defaultList = asList(false, true);
        testWithConfig(CONFIG_KEY, "true, false, baz", config ->
                assertThat(config.getBooleanList(CONFIG_KEY, defaultList)).containsExactly(true, false, false));
    }

    @Test
    public void returnsDefaultBooleanListWithEmptyString() throws Exception {
        final List<Boolean> defaultList = asList(false, true);
        testWithConfig(CONFIG_KEY, "", config ->
                assertThat(config.getBooleanList(CONFIG_KEY, defaultList)).containsExactlyElementsOf(defaultList));
    }

    @Test
    public void parsesIntList() throws Exception {
        testWithConfig(CONFIG_KEY, "0, -76, 105", config ->
                assertThat(config.getIntList(CONFIG_KEY)).containsExactly(0, -76, 105));
    }

    @Test
    public void parsesIntListWithDefault() throws Exception {
        final List<Integer> defaultList = asList(6, 20);
        testWithConfig(CONFIG_KEY, "0, -76, 105", config ->
                assertThat(config.getIntList(CONFIG_KEY, defaultList)).containsExactly(0, -76, 105));
    }

    @Test
    public void returnsDefaultIntListWithEmptyString() throws Exception {
        final List<Integer> defaultList = asList(6, 20);
        testWithConfig(CONFIG_KEY, "", config ->
                assertThat(config.getIntList(CONFIG_KEY, defaultList)).containsExactlyElementsOf(defaultList));
    }

    private static void testWithConfig(final String key, final String value, final Consumer<SunriseConfiguration> test) {
        final Map<String, Object> settings = new HashMap<>();
        settings.put(key, value);
        final SunriseConfiguration config = new SunriseConfiguration(new Configuration(settings));
        test.accept(config);
    }
}
