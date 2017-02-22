package com.commercetools.sunrise.common.models;

import java.util.List;

public class BreadcrumbViewModel extends ViewModel {

    private List<BreadcrumbLinkViewModel> links;

    public BreadcrumbViewModel() {
    }

    public List<BreadcrumbLinkViewModel> getLinks() {
        return links;
    }

    public void setLinks(final List<BreadcrumbLinkViewModel> links) {
        this.links = links;
    }
}
