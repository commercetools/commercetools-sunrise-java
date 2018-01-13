package com.commercetools.sunrise.core;

import com.commercetools.sunrise.core.viewmodels.content.messages.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Http;

public abstract class SunriseController extends Controller {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SunriseController.class);

    /**
     * Saves a message of a certain {@link MessageType} meant to be displayed to the user after a redirection to
     * another page.
     *
     * Example use case: the user submitted a successful form, then the user is redirected to a page where the message
     * informing of the successful action is shown.
     * In this case {@code saveMessage(MessageType.SUCCESS, "The action was successful!")} would be invoked right
     * before the redirection is executed.
     *
     * @param messageType type of the message to be saved (e.g. success, info)
     * @param message the content of the message to be saved
     */
    protected void saveMessage(final MessageType messageType, final String message) {
        final String key = messageType.name();
        final Http.Flash flash = flash();
        if (flash.containsKey(key)) {
            LOGGER.warn("Replacing a message of type \"{}\" within the same request\nOld message: \"{}\"\nNew message: \"{}\"",
                    key, flash.get(key), message);
        }
        flash.put(key, message);
    }
}
