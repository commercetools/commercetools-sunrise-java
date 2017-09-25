package com.commercetools.sunrise.play.inject;

import com.commercetools.sunrise.play.configuration.SunriseConfiguration;
import org.junit.Test;
import play.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseConfigurationTest {
    private static final String CONFIG_KEY = "key";

    @Test
    public void createsSunriseConfiguration() throws Exception {
        final Configuration globalConfig = Configuration.empty();
        assertThat(globalConfig)
                .isInstanceOf(Configuration.class)
                .isNotInstanceOf(SunriseConfiguration.class);
        final Configuration sunriseConfiguration = SunriseConfiguration.of(globalConfig);
        assertThat(sunriseConfiguration).isInstanceOf(SunriseConfiguration.class);
    }

    @Test
    public void ignoresEmptyValues() throws Exception {
        testWithConfig(singletonMap(CONFIG_KEY, "foo, ,baz"), config ->
                assertThat(config.getStringList(CONFIG_KEY)).containsExactly("foo", "baz"));
    }

    @Test
    public void returnsNullWithEmptyString() throws Exception {
        testWithConfig(singletonMap(CONFIG_KEY, " "), config ->
                assertThat(config.getStringList(CONFIG_KEY)).isNull());
    }

    @Test
    public void returnsNullWithJustComma() throws Exception {
        testWithConfig(singletonMap(CONFIG_KEY, ","), config ->
                assertThat(config.getStringList(CONFIG_KEY)).isNull());
    }

    @Test
    public void parsesStringList() throws Exception {
        testWithConfig(singletonMap(CONFIG_KEY, "foo, bar, baz"), config ->
                assertThat(config.getStringList(CONFIG_KEY)).containsExactly("foo", "bar", "baz"));
    }

    @Test
    public void parsesStringListWithDefault() throws Exception {
        final List<String> defaultList = asList("foo", "qux");
        testWithConfig(singletonMap(CONFIG_KEY, "foo, bar, baz"), config ->
                assertThat(config.getStringList(CONFIG_KEY, defaultList)).containsExactly("foo", "bar", "baz"));
    }

    @Test
    public void returnsDefaultStringListWithEmptyConfig() throws Exception {
        final List<String> defaultList = asList("foo", "qux");
        testWithConfig(emptyMap(), config ->
                assertThat(config.getStringList(CONFIG_KEY, defaultList)).containsExactlyElementsOf(defaultList));
    }

    @Test
    public void parsesBooleanList() throws Exception {
        testWithConfig(singletonMap(CONFIG_KEY, "true, false, baz"), config ->
                assertThat(config.getBooleanList(CONFIG_KEY)).containsExactly(true, false, false));
    }

    @Test
    public void parsesBooleanListWithDefault() throws Exception {
        final List<Boolean> defaultList = asList(false, true);
        testWithConfig(singletonMap(CONFIG_KEY, "true, false, baz"), config ->
                assertThat(config.getBooleanList(CONFIG_KEY, defaultList)).containsExactly(true, false, false));
    }

    @Test
    public void returnsDefaultBooleanListWithEmptyConfig() throws Exception {
        final List<Boolean> defaultList = asList(false, true);
        testWithConfig(emptyMap(), config ->
                assertThat(config.getBooleanList(CONFIG_KEY, defaultList)).containsExactlyElementsOf(defaultList));
    }

    @Test
    public void parsesIntList() throws Exception {
        testWithConfig(singletonMap(CONFIG_KEY, "0, -76, 105"), config ->
                assertThat(config.getIntList(CONFIG_KEY)).containsExactly(0, -76, 105));
    }

    @Test
    public void parsesIntListWithDefault() throws Exception {
        final List<Integer> defaultList = asList(6, 20);
        testWithConfig(singletonMap(CONFIG_KEY, "0, -76, 105"), config ->
                assertThat(config.getIntList(CONFIG_KEY, defaultList)).containsExactly(0, -76, 105));
    }

    @Test
    public void returnsDefaultIntListWithEmptyConfig() throws Exception {
        final List<Integer> defaultList = asList(6, 20);
        testWithConfig(emptyMap(), config ->
                assertThat(config.getIntList(CONFIG_KEY, defaultList)).containsExactlyElementsOf(defaultList));
    }

    @Test
    public void returnsConfigAsSunriseConfiguration() throws Exception {
        final Map<String, Object> nestedConfig = singletonMap("foo", "bar");
        testWithConfig(singletonMap(CONFIG_KEY, nestedConfig), globalConfig -> {
            final Configuration configuration = globalConfig.getConfig(CONFIG_KEY);
            assertThat(configuration).isInstanceOf(SunriseConfiguration.class);
            assertThat(configuration.asMap())
                    .hasSize(1)
                    .isEqualTo(nestedConfig);
        });
    }

    @Test
    public void returnsConfigAsSunriseConfigurationWithEmptyConfig() throws Exception {
        testWithConfig(emptyMap(), globalConfig -> {
            final Configuration configuration = globalConfig.getConfig(CONFIG_KEY);
            assertThat(configuration).isNull();
        });
    }

    @Test
    public void returnsConfigListAsSunriseConfigurations() throws Exception {
        final List<Map<String, Object>> nestedConfig = asList(singletonMap("foo", "bar"), singletonMap("x", "y"));
        testWithConfig(singletonMap(CONFIG_KEY, nestedConfig), globalConfig -> {
            final List<Configuration> configurationList = globalConfig.getConfigList(CONFIG_KEY);
            assertThat(configurationList).allMatch(config -> config instanceof SunriseConfiguration);
            assertThat(configurationList)
                    .hasSize(2)
                    .extracting(Configuration::asMap)
                    .isEqualTo(nestedConfig);
        });
    }

    @Test
    public void returnsConfigListAsSunriseConfigurationsWithEmptyConfig() throws Exception {
        testWithConfig(emptyMap(), globalConfig -> {
            final List<Configuration> configuration = globalConfig.getConfigList(CONFIG_KEY);
            assertThat(configuration).isNull();
        });
    }

    @Test
    public void returnsDefaultConfigListAsSunriseConfigurationsWithEmptyConfig() throws Exception {
        final List<Map<String, Object>> defaultConfigAsMap = asList(singletonMap("foo", "bar"), singletonMap("x", "y"));
        final List<Configuration> defaultConfigList = defaultConfigAsMap.stream()
                .map(Configuration::new)
                .collect(Collectors.toList());
        testWithConfig(emptyMap(), globalConfig -> {
            final List<Configuration> configurationList = globalConfig.getConfigList(CONFIG_KEY, defaultConfigList);
            assertThat(configurationList).allMatch(config -> config instanceof SunriseConfiguration);
            assertThat(configurationList)
                    .hasSize(2)
                    .extracting(Configuration::asMap)
                    .isEqualTo(defaultConfigAsMap);
        });
    }

    private static void testWithConfig(final Map<String, Object> config, final Consumer<Configuration> test) {
        test.accept(SunriseConfiguration.of(new Configuration(config)));
    }
}
