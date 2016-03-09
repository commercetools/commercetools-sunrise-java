package common.cms;

import play.i18n.Messages;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PlayCmsPage implements CmsPage {
    private final List<Messages> messagesList;
    private final Optional<String> pageKey;

    PlayCmsPage(final List<Messages> messagesList, final String pageKey) {
        this.messagesList = messagesList;
        this.pageKey = Optional.ofNullable(pageKey);
    }

    @Override
    public Optional<String> get(final String messageKey, final Map<String, Object> hashArgs) {
        final String key = key(messageKey);
        for (final Messages messages : messagesList) {
            if (messages.isDefinedAt(key)) {
                return Optional.of(messages.at(key, hashArgs));
            }
        }
        return Optional.empty();
    }

    private String key(final String messageKey) {
        return pageKey.map(pk -> pk + "." + messageKey).orElse(messageKey);
    }
}
