package com.commercetools.sunrise.core.viewmodels.content.messages;

import com.commercetools.sunrise.core.SunriseController;

/**
 * Types of messages that can be displayed to the user using the built-in Sunrise's messages system.
 *
 * @see SunriseController#saveMessage(MessageType, String)
 */
public enum MessageType {

    SUCCESS,

    WARNING,

    INFO,

    DANGER
}
