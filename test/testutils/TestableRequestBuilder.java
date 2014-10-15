package testutils;

import play.i18n.Lang;
import play.mvc.Http;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestableRequestBuilder {
    private String uri = "";
    private String method = "";
    private String version = "";
    private String remoteAddress = "";
    private boolean secure = false;
    private String host = "";
    private String path = "";
    private List<Lang> acceptLanguages = Collections.emptyList();
    private List<String> accept = Collections.emptyList();
    private List<String> acceptedTypes = Collections.emptyList();
    private Map<String, String[]> queryString = Collections.emptyMap();
    private Http.Cookies cookies = new TestableCookies(Collections.emptyList());
    private Map<String, String[]> headers = Collections.emptyMap();

    public TestableRequestBuilder() {
    }

    public TestableRequest build() {
        return new TestableRequest(uri, method, version, remoteAddress, secure, host, path, acceptLanguages, accept,
                acceptedTypes, queryString, cookies, headers);
    }

    public TestableRequestBuilder uri(String uri) {
        this.uri = uri;
        return this;
    }

    public TestableRequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public TestableRequestBuilder version(String version) {
        this.version = version;
        return this;
    }

    public TestableRequestBuilder remoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public TestableRequestBuilder secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public TestableRequestBuilder host(String host) {
        this.host = host;
        return this;
    }

    public TestableRequestBuilder path(String path) {
        this.path = path;
        return this;
    }

    public TestableRequestBuilder acceptLanguages(List<Lang> acceptLanguages) {
        this.acceptLanguages = acceptLanguages;
        return this;
    }

    public TestableRequestBuilder accept(List<String> accept) {
        this.accept = accept;
        return this;
    }

    public TestableRequestBuilder acceptedTypes(List<String> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
        return this;
    }

    public TestableRequestBuilder queryString(Map<String, String[]> queryString) {
        this.queryString = queryString;
        return this;
    }

    public TestableRequestBuilder cookies(List<Http.Cookie> cookieList) {
        this.cookies = new TestableCookies(cookieList);
        return this;
    }

    public TestableRequestBuilder headers(Map<String, String[]> headers) {
        this.headers = headers;
        return this;
    }
}
