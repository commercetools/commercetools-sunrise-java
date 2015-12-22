package inject;

import com.google.inject.Provider;
import common.cms.CmsService;
import common.cms.PlayCmsService;
import play.Logger;
import play.i18n.MessagesApi;

import javax.inject.Inject;

class CmsServiceProvider implements Provider<CmsService> {
    private final MessagesApi messagesApi;

    @Inject
    private CmsServiceProvider(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Override
    public CmsService get() {
        Logger.debug("Provide PlayCmsService");
        return PlayCmsService.of(messagesApi);
    }
}
