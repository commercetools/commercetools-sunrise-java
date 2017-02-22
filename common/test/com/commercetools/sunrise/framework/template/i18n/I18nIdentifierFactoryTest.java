package com.commercetools.sunrise.framework.template.i18n;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class I18nIdentifierFactoryTest {

    @Test
    public void parsesIdentifier() throws Exception {
        final I18nIdentifier identifier = i18nIdentifierFactory().create("bundle:message.key");
        assertThat(identifier.getBundle()).isEqualTo("bundle");
        assertThat(identifier.getMessageKey()).isEqualTo("message.key");
    }

    @Test
    public void parsesIdentifierWithoutBundle() throws Exception {
        final I18nIdentifier identifier = i18nIdentifierFactory().create("message.key");
        assertThat(identifier.getBundle()).isEqualTo("main");
        assertThat(identifier.getMessageKey()).isEqualTo("message.key");
    }

    private I18nIdentifierFactory i18nIdentifierFactory() {
        return new I18nIdentifierFactory();
    }
}
