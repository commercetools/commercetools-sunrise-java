package common.pages;

import common.context.UserContext;
import org.junit.Test;
import play.i18n.Lang;

import java.util.Locale;

import static com.neovisionaries.i18n.CountryCode.AT;
import static org.assertj.core.api.Assertions.assertThat;

public class UserContextTest {

    @Test
    public void hasCorrespondingLocaleWhenCountryAndLanguageProvided() {
        UserContext context = UserContext.of(lang("de-DE"), AT);
        assertThat(context.locale()).isEqualTo(locale("de", "DE"));
    }

    @Test
    public void hasLocaleWithCountryWhenOnlyLanguageProvided() {
        UserContext context = UserContext.of(lang("de"), AT);
        assertThat(context.locale()).isEqualTo(locale("de", "AT"));
    }

    private Lang lang(String code) {
        return Lang.forCode(code);
    }

    private Locale locale(String language, String country) {
        return new Locale(language, country);
    }
}