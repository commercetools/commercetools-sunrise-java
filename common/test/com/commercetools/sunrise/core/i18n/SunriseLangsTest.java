package com.commercetools.sunrise.core.i18n;

import com.commercetools.sunrise.core.i18n.api.SunriseLangs;
import io.sphere.sdk.projects.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.Configuration;
import play.api.i18n.Langs;
import play.i18n.Lang;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Locale.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SunriseLangsTest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final Locale HINDI = Locale.forLanguageTag("HIN");

    @Mock
    private Project project;

    @Test
    public void takesAvailableFromConfig() {
        mockProjectWithAsianLangs();
        final List<String> configLangs = asList("it", "fr");
        testAvailables(configLangs, langs -> assertThat(langs).containsExactly(ITALIAN, FRENCH));
    }

    @Test
    public void takesAvailableFromProjectIfNotConfigured() {
        mockProjectWithAsianLangs();
        final List<String> configLangs = emptyList();
        testAvailables(configLangs, langs -> assertThat(langs).containsExactly(CHINESE, JAPANESE, HINDI));
    }

    @Test
    public void takesAvailableFromSystemAsFallback() {
        mockProjectWithoutLangs();
        final List<String> configLangs = emptyList();
        testAvailables(configLangs, langs -> assertThat(langs).containsExactly(DEFAULT_LOCALE));
    }

    @Test
    public void selectsPreferredCandidate() {
        final List<Locale> configLangs = asList(CHINESE, JAPANESE);

        final List<Locale> candidates1 = asList(JAPANESE, ITALIAN);
        testPreferred(configLangs, candidates1, preferred -> assertThat(preferred).isEqualTo(JAPANESE));

        final List<Locale> candidates2 = asList(ITALIAN, JAPANESE);
        testPreferred(configLangs, candidates2, preferred -> assertThat(preferred).isEqualTo(JAPANESE));
    }

    @Test
    public void selectsFirstAvailableIfNotMatchingCandidate() {
        final List<Locale> configLangs = asList(CHINESE, JAPANESE);
        final List<Locale> candidates = singletonList(ITALIAN);
        testPreferred(configLangs, candidates, preferred -> assertThat(preferred).isEqualTo(CHINESE));
    }

    @Test
    public void selectsSystemLocaleAsFallback() {
        final List<Locale> configLangs = emptyList();
        final List<Locale> candidates = singletonList(ITALIAN);
        testPreferred(configLangs, candidates, preferred -> assertThat(preferred).isEqualTo(DEFAULT_LOCALE));
    }

    private void mockProjectWithoutLangs() {
        when(project.getLanguageLocales()).thenReturn(emptyList());
    }

    private void mockProjectWithAsianLangs() {
        when(project.getLanguageLocales()).thenReturn(asList(CHINESE, JAPANESE, HINDI));
    }

    private void testAvailables(final List<String> configLangs, final Consumer<List<Locale>> test) {
        final Langs langs = langs(configLangs);
        final List<play.api.i18n.Lang> langAvailables = JavaConversions.bufferAsJavaList(langs.availables().toBuffer());
        test.accept(langAvailables.stream().map(play.api.i18n.Lang::toLocale).collect(toList()));
    }

    private void testPreferred(final List<Locale> configLangs, final List<Locale> candidates, final Consumer<Locale> test) {
        final Langs langs = langs(configLangs.stream().map(Locale::toLanguageTag).collect(toList()));
        final List<play.api.i18n.Lang> langCandidates = candidates.stream().map(Lang::apply).collect(toList());
        test.accept(langs.preferred(JavaConversions.asScalaBuffer(langCandidates).toSeq()).toLocale());
    }

    private Langs langs(final List<String> configLangs) {
        final Configuration config = new Configuration(singletonMap("play.i18n.langs", configLangs));
        return new SunriseLangs(config.getWrappedConfiguration(), () -> project);
    }
}
