package wedecidelatercommon;

import play.mvc.Call;

public interface HomeReverseRouter {
    Call showHome(final String languageTag);
}
