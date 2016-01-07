package purchase;

import common.contexts.UserContext;
import common.i18n.I18nResolver;
import common.models.SelectableData;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SalutationsFieldsBean extends Base {
    private static final String ALLOWED_TITLES_CONFIG_KEY = "checkout.allowedTitles";
    private static final String MESSAGE_CONFIG_KEY = "message";

    private List<SelectableData> list;

    public SalutationsFieldsBean() {
    }

    public SalutationsFieldsBean(final String title, final UserContext userContext, final I18nResolver i18nResolver,
                                 final Configuration configuration) {
        fill(userContext, i18nResolver, configuration, title);
    }

    public SalutationsFieldsBean(@Nullable final Address address, final UserContext userContext,
                                 final I18nResolver i18nResolver, final Configuration configuration) {
        final String title = address == null ? null : address.getSalutation();
        fill(userContext, i18nResolver, configuration, title);
    }

    private void fill(final UserContext userContext, final I18nResolver i18nResolver, final Configuration configuration,
                      @Nullable final String title) {
        final List<SelectableData> selectableDataList = configuration.getObjectList(ALLOWED_TITLES_CONFIG_KEY).stream().map(map -> {
            final String key = map.get(MESSAGE_CONFIG_KEY).toString();
            final String shownTitle = i18nResolver.getOrEmpty("translations", key, userContext.locales());
            final SelectableData selectableData = new SelectableData(shownTitle, shownTitle);
            selectableData.setSelected(selectableData.getValue().equals(title));
            return selectableData;
        }).collect(toList());
        setList(selectableDataList);
    }


    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }

    public String getSelected() {
        return getList().stream()
                .filter(c -> c.isSelected())
                .map(c -> c.getValue())
                .findFirst()
                .orElse(null);
    }
}
