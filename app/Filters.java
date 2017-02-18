import com.commercetools.sunrise.httpauth.HttpAuthenticationFilter;
import play.filters.csrf.CSRFFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(final HttpAuthenticationFilter httpAuthenticationFilter, final CSRFFilter csrfFilter) {
        super(httpAuthenticationFilter, csrfFilter);
    }
}