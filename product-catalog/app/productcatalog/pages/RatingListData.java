package productcatalog.pages;

import java.util.List;

import static java.util.Arrays.asList;

public class RatingListData {

    public String getText() {
        return "ratingData_text";
    }

    public String getUrl() {
        return "ratingData_url";
    }

    public List<RatingData> getList() {
        return asList(new RatingData());
    }
}
