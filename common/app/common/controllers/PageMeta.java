package common.controllers;

import common.models.HalLink;
import play.mvc.Call;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageMeta {
    private String assetsPath;
    private String csrfToken;
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

    public Map<String, HalLink> get_links() {
        return _links;
    }

    public void set_links(final Map<String, HalLink> _links) {
        this._links = _links;
    }

    public PageMeta addHalLink(final Call call, final String rel, final String ... moreRel) {
        return addHalLinkOfHrefAndRel(call.url(), rel, moreRel);
    }

    public PageMeta addHalLinkOfHrefAndRel(final String href, final String rel,  final String ... moreRel) {
        _links.put(rel, new HalLink(href));
        for (int i = 0; i < moreRel.length; i++) {
            addHalLinkOfHrefAndRel(href, moreRel[i]);
        }
        return this;
    }
}