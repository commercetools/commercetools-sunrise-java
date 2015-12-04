package common.models;

import io.sphere.sdk.models.Base;

import java.util.List;

public class MiniCartLineItems extends Base {
    private List<MiniCartLineItem> list;

    public MiniCartLineItems() {
    }

    public MiniCartLineItems(final List<MiniCartLineItem> list) {
        this.list = list;
    }

    public List<MiniCartLineItem> getList() {
        return list;
    }

    public void setList(final List<MiniCartLineItem> list) {
        this.list = list;
    }
}
