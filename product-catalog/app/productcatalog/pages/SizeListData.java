package productcatalog.pages;

import java.util.List;

import static java.util.Arrays.asList;

public class SizeListData {

    public String getText() {
        return "Size";
    }

    public List<SizeData> getList() {
        return asList(new SizeData());
    }
}
