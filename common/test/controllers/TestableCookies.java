package controllers;

import play.mvc.Http;

import java.util.Iterator;
import java.util.List;

public class TestableCookies implements Http.Cookies {

    private final List<Http.Cookie> cookieList;

    TestableCookies(List<Http.Cookie> cookieList) {
        this.cookieList = cookieList;
    }

    @Override
    public Http.Cookie get(String name) {
        for (Http.Cookie cookie : cookieList) {
            if (cookie.name().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    @Override
    public Iterator<Http.Cookie> iterator() {
        return cookieList.iterator();
    }
}
