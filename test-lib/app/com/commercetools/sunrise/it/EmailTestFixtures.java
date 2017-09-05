package com.commercetools.sunrise.it;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public final class EmailTestFixtures {

    private EmailTestFixtures() {
    }

    public static MimeMessage blankMimeMessage() {
        return new MimeMessage(Session.getInstance(new Properties()));
    }

    public static Address addressOf(final String address) throws Exception {
        return new InternetAddress(address);
    }
}
