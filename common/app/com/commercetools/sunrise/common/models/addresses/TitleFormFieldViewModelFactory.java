package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class TitleFormFieldViewModelFactory extends FormFieldViewModelFactory<TitleFormFieldViewModel, String> {

    private final List<String> defaultTitleKeys;
    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final TitleFormSelectableOptionViewModelFactory titleFormSelectableOptionViewModelFactory;


    @Inject
    public TitleFormFieldViewModelFactory(final Configuration configuration, final Locale locale, final I18nResolver i18nResolver,
                                          final I18nIdentifierFactory i18nIdentifierFactory, final TitleFormSelectableOptionViewModelFactory titleFormSelectableOptionViewModelFactory) {
        this.defaultTitleKeys = configuration.getStringList("form.titles", emptyList());
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.titleFormSelectableOptionViewModelFactory = titleFormSelectableOptionViewModelFactory;
    }

    @Override
    protected TitleFormFieldViewModel getViewModelInstance() {
        return new TitleFormFieldViewModel();
    }

    @Override
    protected List<String> defaultOptions() {
        return defaultTitleKeys;
    }

    @Override
    public final TitleFormFieldViewModel create(final FormFieldWithOptions<String> formFieldWithOptions) {
        return super.create(formFieldWithOptions);
    }

    @Override
    public final TitleFormFieldViewModel createWithDefaultOptions(final Form.Field data) {
        return super.createWithDefaultOptions(data);
    }

    @Override
    protected final void initialize(final TitleFormFieldViewModel viewModel, final FormFieldWithOptions<String> formFieldWithOptions) {
        fillList(viewModel, formFieldWithOptions);
    }

    protected void fillList(final TitleFormFieldViewModel viewModel, final FormFieldWithOptions<String> formFieldWithOptions) {
        viewModel.setList(formFieldWithOptions.getFormOptions().stream()
                .map(titleKey -> titleFormSelectableOptionViewModelFactory.create(createTitle(titleKey), formFieldWithOptions.getFormField().value()))
                .collect(toList()));
    }

    private String createTitle(final String titleKey) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(titleKey);
        return i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
    }
}
