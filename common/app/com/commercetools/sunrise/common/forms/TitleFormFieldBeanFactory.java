package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class TitleFormFieldBeanFactory extends Base {

    private static final String CONFIG_TITLE_OPTIONS = "form.titles";
    @Inject
    private Configuration configuration;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;
    @Inject
    private UserContext userContext;

    public TitleFormFieldBean create(final Form<?> form, final String fieldName, final List<String> availableTitles) {
        final TitleFormFieldBean bean = new TitleFormFieldBean();
        initialize(bean, form, fieldName, availableTitles);
        return bean;
    }

    public TitleFormFieldBean createWithDefaultTitles(final Form<?> form, final String fieldName) {
        return create(form, fieldName, getDefaultTitles());
    }

    protected final void initialize(final TitleFormFieldBean bean, final Form<?> form, final String fieldName, final List<String> availableTitles) {
        fillList(bean, form, fieldName, availableTitles);
    }

    protected void fillList(final TitleFormFieldBean bean, final Form<?> form, final String fieldName, final List<String> availableTitles) {
        final String selectedTitle = form.field(fieldName).valueOr(null);
        bean.setList(availableTitles.stream()
                .map(title -> titleToSelectableData(title, selectedTitle))
                .collect(toList()));
    }

    protected FormSelectableOptionBean titleToSelectableData(final String titleKey, @Nullable final String selectedTitle) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(titleKey);
        final String title = i18nResolver.getOrKey(userContext.locales(), i18nIdentifier);
        bean.setLabel(title);
        bean.setValue(title);
        bean.setSelected(title.equals(selectedTitle));
        return bean;
    }

    protected List<String> getDefaultTitles() {
        return configuration.getStringList(CONFIG_TITLE_OPTIONS, emptyList());
    }
}
