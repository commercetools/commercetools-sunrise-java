package purchase;

import common.contexts.UserContext;
import common.pages.SelectableData;
import io.sphere.sdk.models.Address;
import play.Configuration;
import play.i18n.Messages;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SalutationsFieldsBean {
    private static final String ALLOWED_TITLES_CONFIG_KEY = "checkout.allowedTitles";
    private static final String MESSAGE_CONFIG_KEY = "message";

    private List<SelectableData> list;

    public SalutationsFieldsBean() {
    }

    public SalutationsFieldsBean(final String title, final Messages messages, final Configuration configuration) {
        fill(messages, configuration, title);
    }

    public SalutationsFieldsBean(@Nullable final Address address, final UserContext userContext, final Messages messages, final Configuration configuration) {
        final String title = address == null ? null : address.getSalutation();
        fill(messages, configuration, title);
    }

    private void fill(final Messages messages, final Configuration configuration, @Nullable final String title) {
        final List<SelectableData> selectableDataList = configuration.getObjectList(ALLOWED_TITLES_CONFIG_KEY).stream().map(map -> {
            final SelectableData selectableData = new SelectableData();
            final String shownTitle = messages.at(map.get(MESSAGE_CONFIG_KEY).toString());
            selectableData.setLabel(shownTitle);
            selectableData.setValue(shownTitle);
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
