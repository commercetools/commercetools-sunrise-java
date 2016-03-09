package common.cms;

import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * Service that provides content data from Messages files as handled by Play.
 * In order to share a common interface with other CMS, a normal message key has been split into Page Key
 * (required by {@link PlayCmsService#getPage(List, String)}) and Message Key (required by {@link PlayCmsPage#get(String, Object...)}).
 * A possible example: "home.title" is split into "home" as Page Key and "title" as Message Key.
 * Nonetheless, you can always skip the Page Key and provide only a Message Key "home.title".
 */
@Singleton
public final class PlayCmsService implements CmsService {
    private final MessagesApi messagesApi;

    @Inject
    private PlayCmsService(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Override
    public F.Promise<CmsPage> getPage(final List<Locale> locales, final String pageKey) {
        final List<Messages> messagesList = locales.stream().map(locale -> {
            final Lang lang = Lang.forCode(locale.toLanguageTag());
            return new Messages(lang, messagesApi);
        }).collect(toList());
        final CmsPage cmsPage = new PlayCmsPage(messagesList, pageKey);
        return F.Promise.pure(cmsPage);
    }

    public static PlayCmsService of(final MessagesApi messagesApi) {
        return new PlayCmsService(messagesApi);
    }
}