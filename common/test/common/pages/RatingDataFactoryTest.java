package common.pages;

import common.cms.CmsPage;
import common.models.RatingData;
import common.models.RatingDataFactory;
import common.models.SelectableData;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Optional;

public class RatingDataFactoryTest {
    
    @Test
    public void create() {
        final CmsPage cms = (messageKey, args) -> Optional.of(messageKey);

        final RatingData ratingData = RatingDataFactory.of(cms).create();

        Assertions.assertThat(ratingData.getRating()).containsExactly(
                new SelectableData("5 Stars", "5", "ratingFiveStarText", "", false),
                new SelectableData("4 Stars", "4", "ratingFourStarText", "", false),
                new SelectableData("3 Stars", "3", "ratingThreeStarText", "", false),
                new SelectableData("2 Stars", "2", "ratingTwoStarText", "", false),
                new SelectableData("1 Stars", "1", "ratingOneStarText", "", false));
    }
}
