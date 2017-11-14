package com.commercetools.sunrise.framework.viewmodels.content.messages;

/**
 * Types of messages that can be displayed to the user using the built-in Sunrise's messages system.
 *
 * @see com.commercetools.sunrise.framework.controllers.SunriseController#saveMessage(MessageType, String)
 */
public enum MessageType {

    SUCCESS,

    WARNING,

    INFO,

    DANGER
}
