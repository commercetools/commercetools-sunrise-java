package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import play.Configuration;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class TitleFormFieldBeanFactory extends ViewModelFactory {

    private static final String CONFIG_TITLE_OPTIONS = "form.titles";
    private final List<String> defaultTitles;
    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    public TitleFormFieldBeanFactory(final Locale locale, final I18nResolver i18nResolver,
                                     final I18nIdentifierFactory i18nIdentifierFactory, final Configuration configuration) {
        this.defaultTitles = configuration.getStringList(CONFIG_TITLE_OPTIONS, emptyList());
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    public TitleFormFieldBean create(final Form<?> form, final String fieldName, final List<String> availableTitles) {
        final TitleFormFieldBean bean = new TitleFormFieldBean();
        initialize(bean, form, fieldName, availableTitles);
        return bean;
    }

    public TitleFormFieldBean createWithDefaultTitles(final Form<?> form, final String fieldName) {
        return create(form, fieldName, defaultTitles);
    }

    protected final void initialize(final TitleFormFieldBean bean, final Form<?> form, final String fieldName, final List<String> availableTitles) {
        fillList(bean, form, fieldName, availableTitles);
    }

    protected void fillList(final TitleFormFieldBean bean, final Form<?> form, final String fieldName, final List<String> availableTitles) {
        final String selectedTitle = getSelectedTitle(form, fieldName);
        bean.setList(availableTitles.stream()
                .map(title -> createFormSelectableOption(title, selectedTitle))
                .collect(toList()));
    }

    @Nullable
    private String getSelectedTitle(final Form<?> form, final String fieldName) {
        return form.field(fieldName).value();
    }

    private FormSelectableOptionBean createFormSelectableOption(final String titleKey, @Nullable final String selectedTitle) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(titleKey);
        final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
        bean.setLabel(title);
        bean.setValue(title);
        bean.setSelected(title.equals(selectedTitle));
        return bean;
    }
}
