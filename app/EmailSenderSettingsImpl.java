import com.google.inject.Inject;
import io.commercetools.sunrise.email.smtp.SmtpConfiguration;
import io.sphere.sdk.models.Base;
import play.Configuration;

final class EmailSenderSettingsImpl extends Base implements EmailSenderSettings {
    private final SmtpConfiguration smtpConfiguration;
    private final int timeoutMs;

    @Inject
    EmailSenderSettingsImpl(final Configuration globalConfig) {
        this(globalConfig, "email.sender.smtp");
    }

    EmailSenderSettingsImpl(final Configuration globalConfig, final String configPath) {
        final Configuration config = globalConfig.getConfig(configPath);
        final String host = config.getString("host");
        final int port = config.getInt("port");
        final String username = config.getString("username");
        final String password = config.getString("password");
        final String security = config.getString("security");
        smtpConfiguration = new SmtpConfiguration(host, port, SmtpConfiguration.TransportSecurity.valueOf(security), username, password);
        timeoutMs = config.getInt("timeoutMs");
    }

    @Override
    public SmtpConfiguration smtpConfiguration() {
        return smtpConfiguration;
    }

    @Override
    public int timeoutMs() {
        return timeoutMs;
    }
}
