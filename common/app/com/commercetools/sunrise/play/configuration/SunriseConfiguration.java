package com.commercetools.sunrise.play.configuration;

import com.typesafe.config.ConfigException;
import play.Configuration;
import play.api.PlayException;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class SunriseConfiguration extends Configuration {

    private SunriseConfiguration(final Configuration configuration) {
        super(configuration.underlying());
    }

    @Override
    @Nullable
    public List<String> getStringList(final String key) {
        try {
            return super.getStringList(key);
        } catch (PlayException e) {
            return recoverAndParseList(key, SunriseConfiguration::parseStringList, e).orElse(null);
        }
    }

    @Override
    public List<String> getStringList(final String key, final List<String> defaultList) {
        try {
            return super.getStringList(key, defaultList);
        } catch (PlayException e) {
            return recoverAndParseList(key, SunriseConfiguration::parseStringList, e).orElse(defaultList);
        }
    }

    @Nullable
    @Override
    public List<Boolean> getBooleanList(final String key) {
        try {
            return super.getBooleanList(key);
        } catch (PlayException e) {
            return recoverAndParseList(key, SunriseConfiguration::parseBooleanList, e).orElse(null);
        }
    }

    @Override
    public List<Boolean> getBooleanList(final String key, final List<Boolean> defaultList) {
        try {
            return super.getBooleanList(key, defaultList);
        } catch (PlayException e) {
            return recoverAndParseList(key, SunriseConfiguration::parseBooleanList, e).orElse(defaultList);
        }
    }

    @Nullable
    @Override
    public List<Integer> getIntList(final String key) {
        try {
            return super.getIntList(key);
        } catch (PlayException e) {
            return recoverAndParseList(key, SunriseConfiguration::parseIntList, e).orElse(null);
        }
    }

    @Override
    public List<Integer> getIntList(final String key, final List<Integer> defaultList) {
        try {
            return super.getIntList(key, defaultList);
        } catch (PlayException e) {
            return recoverAndParseList(key, SunriseConfiguration::parseIntList, e).orElse(defaultList);
        }
    }

    @Override
    public Configuration getConfig(final String key) {
        return Optional.ofNullable(super.getConfig(key))
                .map(SunriseConfiguration::of)
                .orElse(null);
    }

    @Override
    public List<Configuration> getConfigList(final String key) {
        return convertToSunriseConfigurationList(super.getConfigList(key));
    }

    @Override
    public List<Configuration> getConfigList(final String key, final List<Configuration> defaultList) {
        return convertToSunriseConfigurationList(super.getConfigList(key, defaultList));
    }

    /**
     * Returns the configuration using the implementation of {@link SunriseConfiguration}.
     * @param configuration the original configuration
     * @return the new configuration
     */
    public static Configuration of(final Configuration configuration) {
        return new SunriseConfiguration(configuration);
    }

    private <T> Optional<List<T>> recoverAndParseList(final String key, final Function<String, List<T>> parser,
                                                      final PlayException e) {
        if (e.getCause() instanceof ConfigException.WrongType) {
            return Optional.ofNullable(getString(key))
                    .map(parser)
                    .filter(value -> !value.isEmpty());
        } else {
            throw e;
        }
    }

    private static List<Integer> parseIntList(final String value) {
        return parseList(value)
                .map(Integer::parseInt)
                .collect(toList());
    }

    private static List<Boolean> parseBooleanList(final String value) {
        return parseList(value)
                .map(Boolean::parseBoolean)
                .collect(toList());
    }

    private static List<String> parseStringList(final String value) {
        return parseList(value).collect(toList());
    }

    private static Stream<String> parseList(final String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(v -> !v.isEmpty());
    }

    private static List<Configuration> convertToSunriseConfigurationList(@Nullable final List<Configuration> configurations) {
        return Optional.ofNullable(configurations)
                .map(list -> list.stream()
                        .map(SunriseConfiguration::of)
                        .collect(toList()))
                .orElse(null);
    }
}
