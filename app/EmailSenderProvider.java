import com.google.inject.Provider;
import io.commercetools.sunrise.email.EmailSender;
import io.commercetools.sunrise.email.smtp.SmtpAuthEmailSender;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;


public final class EmailSenderProvider implements Provider<EmailSender> {
    private final EmailSenderSettings emailSenderSettings;

    @Inject
    protected EmailSenderProvider(final EmailSenderSettings emailSenderSettings) {
        this.emailSenderSettings = emailSenderSettings;
    }

    @Override
    public EmailSender get() {
        final Executor executor = ForkJoinPool.commonPool();
        return new SmtpAuthEmailSender(emailSenderSettings.smtpConfiguration(), executor, emailSenderSettings.timeoutMs());
    }
}
