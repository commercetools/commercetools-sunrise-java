package common.pages;

import java.util.List;

public class CollectionData {
    private final String text;
    private final List<SelectableData> list;

    public CollectionData(final String text, final List<SelectableData> list) {
        this.text = text;
        this.list = list;
    }

    public String getText() {
        return text;
    }

    public List<SelectableData> getList() {
        return list;
    }
}
