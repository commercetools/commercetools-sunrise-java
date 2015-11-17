package purchase;

import common.contexts.UserContext;
import common.pages.SelectableData;
import io.sphere.sdk.models.Address;
import play.Configuration;
import play.i18n.Messages;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SalutationsFieldsBean {
    private static final String ALLOWED_TITLES_CONFIG_KEY = "checkout.allowedTitles";
    private static final String MESSAGE_CONFIG_KEY = "message";
    private static final String VALUE_CONFIG_KEY = "value";

    private List<SelectableData> list;


    public SalutationsFieldsBean() {
    }

    public SalutationsFieldsBean(@Nullable final Address address, final UserContext userContext, final Messages messages, final Configuration configuration) {
        final String title = address == null ? null : address.getTitle();

        final List<SelectableData> selectableDataList = configuration.getObjectList(ALLOWED_TITLES_CONFIG_KEY).stream().map(map -> {
            final SelectableData selectableData = new SelectableData();
            selectableData.setText(messages.at(map.get(MESSAGE_CONFIG_KEY).toString()));
            selectableData.setValue(map.get(VALUE_CONFIG_KEY).toString());
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
}
