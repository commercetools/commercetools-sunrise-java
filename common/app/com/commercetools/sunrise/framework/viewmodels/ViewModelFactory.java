package com.commercetools.sunrise.framework.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.messages.MessageType;
import com.commercetools.sunrise.framework.viewmodels.content.messages.MessageViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static play.mvc.Http.Context.Implicit.flash;

public abstract class ViewModelFactory {

    /**
     * Extracts all messages saved via
     * {@link com.commercetools.sunrise.framework.controllers.SunriseController#saveMessage(MessageType, String)}
     * and returns a list of view models with those messages. These view models can be set for example in the
     * {@link com.commercetools.sunrise.framework.viewmodels.content.PageContent} using
     * {@link com.commercetools.sunrise.framework.viewmodels.content.PageContent#setMessages(List)}
     *
     * @return list of view models with the extracted messages
     */
    protected static List<MessageViewModel> extractMessages() {
        return extractMessages(flash());
    }

    private static List<MessageViewModel> extractMessages(final Map<String, String> map) {
        final List<MessageViewModel> messageViewModels = new ArrayList<>();
        Stream.of(MessageType.values())
                .forEach(type -> findMessage(type, map)
                        .ifPresent(messageViewModels::add));
        return messageViewModels;
    }

    private static Optional<MessageViewModel> findMessage(final MessageType messageType, final Map<String, String> map) {
        final String type = messageType.name();
        return Optional.ofNullable(map.get(type))
                .map(message -> createMessage(type, message));
    }

    private static MessageViewModel createMessage(final String type, final String message) {
        final MessageViewModel messageViewModel = new MessageViewModel();
        messageViewModel.setMessage(message);
        messageViewModel.setType(type.toLowerCase());
        return messageViewModel;
    }
}
