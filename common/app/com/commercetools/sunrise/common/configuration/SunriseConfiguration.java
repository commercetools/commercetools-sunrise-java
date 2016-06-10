package com.commercetools.sunrise.common.configuration;

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

public class SunriseConfiguration extends Configuration {

    public SunriseConfiguration(final Configuration configuration) {
        super(configuration.underlying());
    }

    @Override
    @Nullable
    public List<String> getStringList(final String key) {
        try {
            return super.getStringList(key);
        } catch (PlayException e) {
            return recoverAndParseList(key, this::parseStringList, e).orElse(null);
        }
    }

    @Override
    public List<String> getStringList(final String key, final List<String> defaultList) {
        try {
            return super.getStringList(key, defaultList);
        } catch (PlayException e) {
            return recoverAndParseList(key, this::parseStringList, e).orElse(defaultList);
        }
    }

    @Nullable
    @Override
    public List<Boolean> getBooleanList(final String key) {
        try {
            return super.getBooleanList(key);
        } catch (PlayException e) {
            return recoverAndParseList(key, this::parseBooleanList, e).orElse(null);
        }
    }

    @Override
    public List<Boolean> getBooleanList(final String key, final List<Boolean> defaultList) {
        try {
            return super.getBooleanList(key, defaultList);
        } catch (PlayException e) {
            return recoverAndParseList(key, this::parseBooleanList, e).orElse(defaultList);
        }
    }

    @Nullable
    @Override
    public List<Integer> getIntList(final String key) {
        try {
            return super.getIntList(key);
        } catch (PlayException e) {
            return recoverAndParseList(key, this::parseIntList, e).orElse(null);
        }
    }

    @Override
    public List<Integer> getIntList(final String key, final List<Integer> defaultList) {
        try {
            return super.getIntList(key, defaultList);
        } catch (PlayException e) {
            return recoverAndParseList(key, this::parseIntList, e).orElse(defaultList);
        }
    }

    private <T> Optional<List<T>> recoverAndParseList(final String key, final Function<String, List<T>> parser,
                                                      final PlayException e) {
        if (e.getCause() instanceof ConfigException.WrongType) {
            return Optional.ofNullable(getString(key))
                    .map(parser::apply)
                    .filter(value -> !value.isEmpty());
        } else {
            throw e;
        }
    }

    private List<Integer> parseIntList(final String value) {
        return parseList(value)
                .map(Integer::parseInt)
                .collect(toList());
    }

    private List<Boolean> parseBooleanList(final String value) {
        return parseList(value)
                .map(Boolean::parseBoolean)
                .collect(toList());
    }

    private List<String> parseStringList(final String value) {
        return parseList(value).collect(toList());
    }

    private static Stream<String> parseList(final String value) {
        return Arrays.asList(value.split(",")).stream()
                .map(String::trim)
                .filter(v -> !v.isEmpty());
    }
}
