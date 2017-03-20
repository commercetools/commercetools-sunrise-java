package com.commercetools.sunrise.framework.viewmodels.meta;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.customers.UserInfoViewModel;
import play.mvc.Call;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageMeta extends ViewModel {

    private String assetsPath;
    private String csrfToken;
    private UserInfoViewModel user;
    private List<Integer> bagQuantityOptions;
    private Map<String, HalLink> _links = new HashMap<>();

    public PageMeta() {
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(final String assetsPath) {
        this.assetsPath = assetsPath;
    }

    public List<Integer> getBagQuantityOptions() {
        return bagQuantityOptions;
    }

    public void setBagQuantityOptions(final List<Integer> bagQuantityOptions) {
        this.bagQuantityOptions = bagQuantityOptions;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public UserInfoViewModel getUser() {
        return user;
    }

    public void setUser(final UserInfoViewModel user) {
        this.user = user;
    }

    public Map<String, HalLink> get_links() {
        return _links;
    }

    public void set_links(final Map<String, HalLink> _links) {
        this._links = _links;
    }

    public PageMeta addHalLink(final Call call, final String rel, final String ... moreRel) {
        return addHalLinkOfHrefAndRel(call.url(), rel, moreRel);
    }

    public PageMeta addHalLinkOfHrefAndRel(final String href, final String rel,  final String ... moreRels) {
        _links.put(rel, new HalLink(href));
        for (final String moreRel : moreRels) {
            addHalLinkOfHrefAndRel(href, moreRel);
        }
        return this;
    }
}