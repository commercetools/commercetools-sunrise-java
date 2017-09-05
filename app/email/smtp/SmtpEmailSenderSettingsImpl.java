package email.smtp;

import com.commercetools.sunrise.email.smtp.SmtpConfiguration;
import com.google.inject.Inject;
import io.sphere.sdk.models.Base;
import play.Configuration;

final class SmtpEmailSenderSettingsImpl extends Base implements SmtpEmailSenderSettings {
    private final SmtpConfiguration smtpConfiguration;
    private final int timeoutMs;

    @Inject
    SmtpEmailSenderSettingsImpl(final Configuration globalConfig) {
        this(globalConfig, "email.sender.smtp");
    }

    SmtpEmailSenderSettingsImpl(final Configuration globalConfig, final String configPath) {
        final Configuration config = globalConfig.getConfig(configPath);
        final String host = config.getString("host");
        final int port = config.getInt("port");
        final String username = config.getString("username");
        final String password = config.getString("password");
        final SmtpConfiguration.TransportSecurity security = getTransportSecurity(config);
        smtpConfiguration = new SmtpConfiguration(host, port, security, username, password);
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

    private static SmtpConfiguration.TransportSecurity getTransportSecurity(final Configuration config) {
        final String security = config.getString("security");
        return SmtpConfiguration.TransportSecurity.valueOf(security);
    }
}
