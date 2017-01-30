package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class TitleFormFieldBeanFactory extends FormFieldViewModelFactory<TitleFormFieldBean, String> {

    private final List<String> defaultTitleKeys;
    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final TitleFormSelectableOptionBeanFactory titleFormSelectableOptionBeanFactory;


    @Inject
    public TitleFormFieldBeanFactory(final Configuration configuration, final Locale locale, final I18nResolver i18nResolver,
                                     final I18nIdentifierFactory i18nIdentifierFactory, final TitleFormSelectableOptionBeanFactory titleFormSelectableOptionBeanFactory) {
        this.defaultTitleKeys = configuration.getStringList("form.titles", emptyList());
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.titleFormSelectableOptionBeanFactory = titleFormSelectableOptionBeanFactory;
    }

    @Override
    protected TitleFormFieldBean getViewModelInstance() {
        return new TitleFormFieldBean();
    }

    @Override
    protected List<String> defaultOptions() {
        return defaultTitleKeys;
    }

    @Override
    public final TitleFormFieldBean create(final FormFieldWithOptions<String> data) {
        return super.create(data);
    }

    @Override
    public final TitleFormFieldBean createWithDefaultOptions(final Form.Field data) {
        return super.createWithDefaultOptions(data);
    }

    @Override
    protected final void initialize(final TitleFormFieldBean model, final FormFieldWithOptions<String> data) {
        fillList(model, data);
    }

    protected void fillList(final TitleFormFieldBean model, final FormFieldWithOptions<String> formFieldWithOptions) {
        model.setList(formFieldWithOptions.getFormOptions().stream()
                .map(titleKey -> titleFormSelectableOptionBeanFactory.create(createTitle(titleKey), formFieldWithOptions.getFormField().value()))
                .collect(toList()));
    }

    private String createTitle(final String titleKey) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(titleKey);
        return i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
    }
}
