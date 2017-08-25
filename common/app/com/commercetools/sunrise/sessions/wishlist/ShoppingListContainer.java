package com.commercetools.sunrise.sessions.wishlist;

import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModel;
import io.sphere.sdk.models.Base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShoppingListContainer extends Base implements Serializable {

    private Map<String, ShoppinglistViewModel> shoppingListsMap = new HashMap<>();

    public Map<String, ShoppinglistViewModel> getShoppingListsMap() {
        return shoppingListsMap;
    }

    public void setShoppingListsMap(Map<String, ShoppinglistViewModel> shoppingListsMap) {
        this.shoppingListsMap = shoppingListsMap;
    }

    public Optional<ShoppinglistViewModel> getShoppingList(String shoppingListType) {
        return Optional.ofNullable(shoppingListsMap.get(shoppingListType));
    }

    public Optional<String> getShoppingListId(String shoppingListType) {
        return getShoppingList(shoppingListType).map(ShoppinglistViewModel::getId);
    }

    public void addShoppingList(String shoppingListType, ShoppinglistViewModel shoppinglistViewModel){
        shoppingListsMap.put(shoppingListType, shoppinglistViewModel);
    }

    public void removeShoppingList(String shoppingListType){
        shoppingListsMap.remove(shoppingListType);
    }

    public void clear(){
        shoppingListsMap.clear();
    }

}
