package productcatalog.pages;

import java.util.List;

import static java.util.Arrays.asList;

public class ColorListData {

    public String getText() {
        return "Color";
    }

    public List<ColorData> getList() {
        return asList(new ColorData());
    }
}
