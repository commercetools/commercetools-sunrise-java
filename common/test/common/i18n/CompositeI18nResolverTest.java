package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class CompositeI18nResolverTest {

    @Test
    public void resolvesWithFirstResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(
                (locale, bundle, key, hashArgs) -> Optional.of("foo"),
                (locale, bundle, key, hashArgs) -> Optional.of("bar"));
        testCompositeResolver(i18nResolverList, message -> assertThat(message).contains("foo"));
    }

    @Test
    public void fallbacksToSecondResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(
                (locale, bundle, key, hashArgs) -> Optional.empty(),
                (locale, bundle, key, hashArgs) -> Optional.of("bar"));
        testCompositeResolver(i18nResolverList, message -> assertThat(message).contains("bar"));
    }

    @Test
    public void emptyWhenNotFoundInAnyResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(
                (locale, bundle, key, hashArgs) -> Optional.empty(),
                (locale, bundle, key, hashArgs) -> Optional.empty());
        testCompositeResolver(i18nResolverList, message -> assertThat(message).isEmpty());
    }

    public void testCompositeResolver(final List<I18nResolver> i18nResolverList, final Consumer<Optional<String>> test) {
        final CompositeI18nResolver i18nResolver = CompositeI18nResolver.of(i18nResolverList);
        final Optional<String> message = i18nResolver.get(singletonList(ENGLISH), "bundle", "key");
        test.accept(message);
    }

}
