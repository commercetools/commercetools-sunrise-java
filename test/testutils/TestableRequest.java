package testutils;

import play.api.http.MediaRange;
import play.i18n.Lang;
import play.mvc.Http;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestableRequest extends Http.Request {
    private final String uri;
    private final String method;
    private final String version;
    private final String remoteAddress;
    private final boolean secure;
    private final String host;
    private final String path;
    private final List<Lang> acceptLanguages;
    private final List<String> accept;
    private final List<String> acceptedTypes;
    private final Map<String, String[]> queryString;
    private final Http.Cookies cookies;
    private final Map<String, String[]> headers;

    TestableRequest(String uri, String method, String version, String remoteAddress, boolean secure, String host,
                    String path, List<Lang> acceptLanguages, List<String> accept, List<String> acceptedTypes,
                    Map<String, String[]> queryString, Http.Cookies cookies, Map<String, String[]> headers) {
        this.uri = uri;
        this.method = method;
        this.version = version;
        this.remoteAddress = remoteAddress;
        this.secure = secure;
        this.host = host;
        this.path = path;
        this.acceptLanguages = acceptLanguages;
        this.accept = accept;
        this.acceptedTypes = acceptedTypes;
        this.queryString = queryString;
        this.cookies = cookies;
        this.headers = headers;
    }

    @Override
    public Http.RequestBody body() {
        return new Http.RequestBody();
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public String remoteAddress() {
        return remoteAddress;
    }

    @Override
    public boolean secure() {
        return secure;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public List<Lang> acceptLanguages() {
        return acceptLanguages;
    }

    @Override
    public List<String> accept() {
        return accept;
    }

    @Override
    public List<MediaRange> acceptedTypes() {
        return Collections.emptyList();
    }

    @Override
    public boolean accepts(String mimeType) {
        return acceptedTypes.contains(mimeType);
    }

    @Override
    public Map<String, String[]> queryString() {
        return queryString;
    }

    @Override
    public Http.Cookies cookies() {
        return cookies;
    }

    @Override
    public Map<String, String[]> headers() {
        return headers;
    }
}
