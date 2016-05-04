package common.models;

import common.contexts.UserContext;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class TitleFormFieldBean extends Base {

    private static final String CONFIG_TITLE_OPTIONS = "form.titles";

    private List<FormSelectableOptionBean> list;

    public TitleFormFieldBean() {
    }

    public TitleFormFieldBean(@Nullable final String selectedTitle, final UserContext userContext,
                              final I18nResolver i18nResolver, final Configuration configuration) {
        this.list = configuration.getStringList(CONFIG_TITLE_OPTIONS, emptyList()).stream()
                .map(title -> titleToSelectableData(title, selectedTitle, i18nResolver, userContext))
                .collect(toList());
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }

    private FormSelectableOptionBean titleToSelectableData(final String titleKey, final @Nullable String selectedTitle,
                                                           final I18nResolver i18nResolver, final UserContext userContext) {
        final String title = i18nResolver.getOrKey(userContext.locales(), I18nIdentifier.of(titleKey));
        final FormSelectableOptionBean formSelectableOptionBean = new FormSelectableOptionBean(title, title);
        if (title.equals(selectedTitle)) {
            formSelectableOptionBean.setSelected(true);
        }
        return formSelectableOptionBean;
    }
}
