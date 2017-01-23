package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldFactory;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class TitleFormFieldBeanFactory extends FormFieldFactory<TitleFormFieldBean, TitleFormFieldBeanFactory.Data> {

    private final List<String> defaultTitleKeys;
    private final TitleFormSelectableOptionBeanFactory titleFormSelectableOptionBeanFactory;

    @Inject
    public TitleFormFieldBeanFactory(final Configuration configuration, final TitleFormSelectableOptionBeanFactory titleFormSelectableOptionBeanFactory) {
        this.defaultTitleKeys = configuration.getStringList("form.titles", emptyList());
        this.titleFormSelectableOptionBeanFactory = titleFormSelectableOptionBeanFactory;
    }

    public final TitleFormFieldBean create(final Form<?> form, final String formFieldName, final List<String> availableTitleKeys) {
        final Data data = new Data(form, formFieldName, availableTitleKeys);
        return initializedViewModel(data);
    }

    public final TitleFormFieldBean createWithDefaultTitles(final Form<?> form, final String formFieldName) {
        final Data data = new Data(form, formFieldName, defaultTitleKeys);
        return initializedViewModel(data);
    }

    @Override
    protected TitleFormFieldBean getViewModelInstance() {
        return new TitleFormFieldBean();
    }

    @Override
    protected final void initialize(final TitleFormFieldBean bean, final Data data) {
        fillList(bean, data);
    }

    protected void fillList(final TitleFormFieldBean bean, final Data data) {
        final String selectedTitleKey = FormUtils.extractFormField(data.form, data.formFieldName);
        bean.setList(data.availableTitleKeys.stream()
                .map(titleKey -> titleFormSelectableOptionBeanFactory.create(titleKey, selectedTitleKey))
                .collect(toList()));
    }

    protected final static class Data extends Base {

        public final Form<?> form;
        public final String formFieldName;
        public final List<String> availableTitleKeys;

        public Data(final Form<?> form, final String formFieldName, final List<String> availableTitleKeys) {
            this.form = form;
            this.formFieldName = formFieldName;
            this.availableTitleKeys = availableTitleKeys;
        }
    }
}
