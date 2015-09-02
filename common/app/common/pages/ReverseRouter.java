package common.pages;

import play.mvc.Call;

public interface ReverseRouter {

    Call subCategory(final String language, final String rootSlug, final String slug, final int page);

    Call category(final String language, final String slug, final int page);
}
