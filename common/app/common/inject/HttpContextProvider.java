package common.inject;

import play.mvc.Http;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class HttpContextProvider implements Provider<Http.Context> {
    @Override
    public Http.Context get() {
        return Http.Context.current();
    }
}
