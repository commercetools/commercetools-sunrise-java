package common.controllers;

import common.models.LocationSelector;
import common.models.MiniCart;
import common.models.NavMenuData;
import io.sphere.sdk.models.Base;

public class PageHeader extends Base {
    private String title;
    private LocationSelector location;
    private MiniCart miniCart;
    private NavMenuData navMenu;

    public PageHeader() {
    }

    public PageHeader(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public LocationSelector getLocation() {
        return location;
    }

    public void setLocation(final LocationSelector location) {
        this.location = location;
    }

    public MiniCart getMiniCart() {
        return miniCart;
    }

    public void setMiniCart(final MiniCart miniCart) {
        this.miniCart = miniCart;
    }

    public NavMenuData getNavMenu() {
        return navMenu;
    }

    public void setNavMenu(final NavMenuData navMenu) {
        this.navMenu = navMenu;
    }
}
