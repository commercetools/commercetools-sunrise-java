import com.google.inject.ImplementedBy;
import io.commercetools.sunrise.email.smtp.SmtpConfiguration;
import play.Configuration;

/**
 * Configuration for {@link io.commercetools.sunrise.email.EmailSender}.
 */
@ImplementedBy(EmailSenderSettingsImpl.class)
public interface EmailSenderSettings {
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
    static EmailSenderSettings of(final Configuration configuration, final String configPath) {
        return new EmailSenderSettingsImpl(configuration, configPath);
    }
}
