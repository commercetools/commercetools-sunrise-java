package common.contexts;

import common.template.i18n.I18nResolver;
import play.Configuration;

import javax.inject.Inject;

public abstract class SunriseDataBeanFactory {
    @Inject
    protected UserContext userContext;
    @Inject
    protected ProjectContext projectContext;
    @Inject
    protected I18nResolver i18nResolver;
    @Inject
    protected Configuration configuration;
}
