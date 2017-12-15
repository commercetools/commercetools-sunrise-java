package com.commercetools.sunrise.core.i18n;

import org.apache.commons.lang3.ArrayUtils;
import play.api.i18n.Lang;
import play.i18n.MessagesApi;
import scala.Tuple2;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Singleton
public class SunriseMessagesApi extends MessagesApi {

    @Inject
    public SunriseMessagesApi(final play.api.i18n.MessagesApi messages) {
        super(messages);
    }

    @Override
    public String get(final Lang lang, final String key, final Object... args) {
        return super.get(lang, key, convertArgsToListOfScalaTuples(args));
    }

    @Override
    public String get(final Lang lang, final List<String> keys, final Object... args) {
        return super.get(lang, keys, convertArgsToListOfScalaTuples(args));
    }

    /**
     * Converts the provided args to a list of scala {@link Tuple2}, if the args correspond to a {@link Map}.
     * @param args the message arguments
     * @return the list of scala tuples or the given args if it is not a map
     */
    private static List<Object> convertArgsToListOfScalaTuples(final Object... args) {
        if (ArrayUtils.isNotEmpty(args) && args.length == 1 && args[0] instanceof Map) {
            final Map<Object, Object> map = castToMap(args[0]);
            return convertToListOfScalaTuples(map);
        }
        return asList(args);
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, Object> castToMap(final Object arg) {
        return (Map<Object, Object>) arg;
    }

    private static List<Object> convertToListOfScalaTuples(final Map<Object, Object> map) {
        return map.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), entry.getValue()))
                .collect(toList());
    }
}
