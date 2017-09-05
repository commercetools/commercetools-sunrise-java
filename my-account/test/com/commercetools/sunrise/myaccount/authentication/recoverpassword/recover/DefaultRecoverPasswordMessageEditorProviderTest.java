package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.template.engine.EmailContentRenderer;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailContent;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailContentFactory;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.customers.CustomerToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.it.EmailTestFixtures.addressOf;
import static com.commercetools.sunrise.it.EmailTestFixtures.blankMimeMessage;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecoverPasswordMessageEditorProviderTest {

    private static final String FROM_FIELD = "sender@mail.com";
    private static final String SUBJECT_FIELD = "Some subject";
    private static final String RECIPIENT_FIELD = "someone@mail.com";
    private static final String CONTENT_FIELD = "Some email content";

    @Mock
    private I18nIdentifierResolver i18nIdentifierResolver;
    @Mock
    private EmailContentRenderer emailContentRendererWithSomeContent;
    @Mock
    private RecoverPasswordEmailContentFactory dummyEmailContentFactory;

    @InjectMocks
    private DefaultRecoverPasswordMessageEditorProvider defaultMessageEditorProvider;
    @InjectMocks
    private CustomRecoverPasswordMessageEditorProvider customMessageEditorProvider;

    @Mock
    private CustomerToken dummyResetPasswordToken;
    @Mock
    private RecoverPasswordFormData formDataWithValidEmail;

    @Before
    public void setUp() throws Exception {
        when(dummyEmailContentFactory.create(notNull())).thenReturn(new RecoverPasswordEmailContent());
        when(formDataWithValidEmail.email()).thenReturn(RECIPIENT_FIELD);
        when(emailContentRendererWithSomeContent.render(notNull(), notNull())).thenReturn(completedFuture(new Html(CONTENT_FIELD)));
    }

    @Test
    public void providesMessageEditor() throws Exception {
        mockI18nResolverThatFindsFromAndSubjectFields();

        final MimeMessage message = blankMimeMessage();
        defaultMessageEditor().edit(message);

        assertThat(message.getSubject()).isEqualTo(SUBJECT_FIELD);
        assertThat(message.getFrom()).contains(addressOf(FROM_FIELD));
        assertThat(message.getContent()).isEqualTo(CONTENT_FIELD);
        assertThat(message.getAllRecipients()).contains(addressOf(RECIPIENT_FIELD));

        verify(dummyEmailContentFactory).create(dummyResetPasswordToken);
    }

    @Test
    public void providesMessageEditorWhenUndefinedFields() throws Exception {
        final MimeMessage message = blankMimeMessage();
        defaultMessageEditor().edit(message);

        assertThat(message.getSubject()).isNull();
        assertThat(message.getFrom()).isNull();
        assertThat(message.getContent()).isEqualTo(CONTENT_FIELD);
        assertThat(message.getAllRecipients()).contains(addressOf(RECIPIENT_FIELD));

        verify(dummyEmailContentFactory).create(dummyResetPasswordToken);
    }

    @Test
    public void customMessageEditorCanOverrideFields() throws Exception {
        mockI18nResolverThatFindsFromAndSubjectFields();

        final MimeMessage message = blankMimeMessage();
        customMessageEditor().edit(message);

        assertThat(message.getSubject())
                .isNotEqualTo(SUBJECT_FIELD)
                .isEqualTo("Another subject");
        assertThat(message.getFrom()).contains(addressOf(FROM_FIELD));
        assertThat(message.getContent()).isEqualTo(CONTENT_FIELD);
        assertThat(message.getAllRecipients()).contains(addressOf(RECIPIENT_FIELD));
        assertThat(message.getDescription()).isEqualTo("Some description");

        verify(dummyEmailContentFactory).create(dummyResetPasswordToken);
    }

    private MessageEditor defaultMessageEditor() throws Exception {
        return defaultMessageEditorProvider.get(dummyResetPasswordToken, formDataWithValidEmail).toCompletableFuture().get();
    }

    private MessageEditor customMessageEditor() throws Exception {
        return customMessageEditorProvider.get(dummyResetPasswordToken, formDataWithValidEmail).toCompletableFuture().get();
    }

    private void mockI18nResolverThatFindsFromAndSubjectFields() {
        when(i18nIdentifierResolver.resolve(eq("my-account:forgotPassword.email.from")))
                .thenReturn(Optional.of(FROM_FIELD));
        when(i18nIdentifierResolver.resolve(eq("my-account:forgotPassword.email.subject")))
                .thenReturn(Optional.of(SUBJECT_FIELD));
    }

    private static class CustomRecoverPasswordMessageEditorProvider extends DefaultRecoverPasswordMessageEditorProvider {

        @Inject
        public CustomRecoverPasswordMessageEditorProvider(final I18nIdentifierResolver i18nIdentifierResolver,
                                                          final EmailContentRenderer emailContentRenderer,
                                                          final RecoverPasswordEmailContentFactory recoverPasswordEmailContentFactory) {
            super(i18nIdentifierResolver, emailContentRenderer, recoverPasswordEmailContentFactory);
        }

        @Override
        public CompletionStage<MessageEditor> get(final CustomerToken resetPasswordToken, final RecoverPasswordFormData formData) {
            // Replaces subject and sets description
            return super.get(resetPasswordToken, formData)
                    .thenApply(msgEditor -> msg -> {
                        msgEditor.edit(msg);
                        msg.setSubject("Another subject");
                        msg.setDescription("Some description");
                    });
        }
    }
}
