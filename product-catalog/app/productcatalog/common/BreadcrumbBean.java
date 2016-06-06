package productcatalog.common;

import common.models.LinkBean;

import java.util.List;

public class BreadcrumbBean {

    private List<LinkBean> links;

    public BreadcrumbBean() {
    }

    public List<LinkBean> getLinks() {
        return links;
    }

    public void setLinks(final List<LinkBean> links) {
        this.links = links;
    }
}
