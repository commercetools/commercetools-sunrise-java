package common.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class RatingData extends Base {
    private final List<SelectableData> rating;

    public  RatingData(final List<SelectableData> rating) {
        this.rating = rating;
    }

    public List<SelectableData> getRating() {
        return rating;
    }
}
