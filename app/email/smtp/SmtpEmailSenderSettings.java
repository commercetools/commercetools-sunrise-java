package email.smtp;

import com.google.inject.ImplementedBy;
import com.commercetools.sunrise.email.smtp.SmtpConfiguration;
import play.Configuration;

/**
 * Configuration for {@link com.commercetools.sunrise.email.EmailSender}.
 */
@ImplementedBy(SmtpEmailSenderSettingsImpl.class)
public interface SmtpEmailSenderSettings {
    /**
     * @return the smtp configuration
     */
    SmtpConfiguration smtpConfiguration();

    /**
     * @return the timeout in ms.
     */
    int timeoutMs();

    /**
     * Creates a new instance from the given configuration.
     *
     * @param configuration the play configuration
     * @param configPath    the path to the config
     * @return a new instance created from the given configuration
     */
    static SmtpEmailSenderSettings of(final Configuration configuration, final String configPath) {
        return new SmtpEmailSenderSettingsImpl(configuration, configPath);
    }
}
