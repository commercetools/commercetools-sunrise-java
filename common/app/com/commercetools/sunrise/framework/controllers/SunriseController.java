package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.viewmodels.content.messages.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.*;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final Logger getLogger() {
        return logger;
    }

    protected final CompletionStage<Result> redirectToCall(final Call call) {
        return completedFuture(Results.redirect(call));
    }

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
            getLogger().warn("Replacing a message of type \"{}\" within the same request\nOld message: \"{}\"\nNew message: \"{}\"",
                    key, flash.get(key), message);
        }
        flash.put(key, message);
    }
}
