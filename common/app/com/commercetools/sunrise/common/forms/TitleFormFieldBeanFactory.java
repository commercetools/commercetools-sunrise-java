package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class TitleFormFieldBeanFactory extends FormFieldViewModelFactory<TitleFormFieldBean, String> {

    private final List<String> defaultTitleKeys;
    private final TitleFormSelectableOptionBeanFactory titleFormSelectableOptionBeanFactory;

    @Inject
    public TitleFormFieldBeanFactory(final Configuration configuration, final TitleFormSelectableOptionBeanFactory titleFormSelectableOptionBeanFactory) {
        this.defaultTitleKeys = configuration.getStringList("form.titles", emptyList());
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
        model.setList(formFieldWithOptions.formOptions.stream()
                .map(titleKey -> titleFormSelectableOptionBeanFactory.create(titleKey, formFieldWithOptions.formField.value()))
                .collect(toList()));
    }
}
