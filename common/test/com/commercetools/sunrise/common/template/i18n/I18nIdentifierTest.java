package com.commercetools.sunrise.common.template.i18n;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class I18nIdentifierTest {

    @Test
    public void parsesIdentifier() throws Exception {
        final I18nIdentifier identifier = I18nIdentifier.of("bundle:message.key");
        assertThat(identifier.getBundle()).isEqualTo("bundle");
        assertThat(identifier.getMessageKey()).isEqualTo("message.key");
    }

    @Test
    public void parsesIdentifierWithoutBundle() throws Exception {
        final I18nIdentifier identifier = I18nIdentifier.of("message.key");
        assertThat(identifier.getBundle()).isEqualTo("main");
        assertThat(identifier.getMessageKey()).isEqualTo("message.key");
    }
}
