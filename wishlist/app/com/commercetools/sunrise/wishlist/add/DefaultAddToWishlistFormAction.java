package com.commercetools.sunrise.wishlist.add;

import com.commercetools.sunrise.models.shoppinglists.MyWishlistCreator;
import com.commercetools.sunrise.models.shoppinglists.MyWishlistUpdater;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.json.JsonException;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.json.TypeReferences;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.ResourceIdentifier;
import io.sphere.sdk.shoppinglists.*;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;
import io.sphere.sdk.types.CustomFields;
import io.sphere.sdk.types.CustomFieldsDraft;
import io.sphere.sdk.types.Type;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

final class DefaultAddToWishlistFormAction implements AddToWishlistFormAction {

    private final MyWishlistUpdater myWishlistUpdater;
    private final MyWishlistCreator myWishlistCreator;

    @Inject
    DefaultAddToWishlistFormAction(final MyWishlistUpdater myWishlistUpdater, final MyWishlistCreator myWishlistCreator) {
        this.myWishlistUpdater = myWishlistUpdater;
        this.myWishlistCreator = myWishlistCreator;
    }

    @Override
    public CompletionStage<ShoppingList> apply(final AddToWishlistFormData formData) {
        return myWishlistUpdater.apply(formData.addLineItem())
                .thenComposeAsync(wishlistOpt -> wishlistOpt
                        .map(wishlist -> (CompletionStage<ShoppingList>) completedFuture(wishlist))
                        .orElseGet(() -> myWishlistCreator.get(buildShoppingListDraft(formData))), HttpExecution.defaultContext());
    }

    private ShoppingListDraft buildShoppingListDraft(final AddToWishlistFormData formData) {
        return ShoppingListDraftBuilder.of(LocalizedString.ofEnglish("Wishlist"))
                .lineItems(singletonList(buildLineItemDraft(formData)))
                .build();
    }

    private LineItemDraft buildLineItemDraft(final AddToWishlistFormData formData) {
        final AddLineItem addLineItem = formData.addLineItem();
        return LineItemDraftBuilder.of(addLineItem.getProductId())
                .variantId(addLineItem.getVariantId())
                .quantity(addLineItem.getQuantity())
                .custom(customFields(addLineItem.getCustom()))
                .addedAt(addLineItem.getAddedAt())
                .build();
    }

    // TODO Remove this once JVM SDK has consistency on drafts (https://github.com/commercetools/commercetools-jvm-sdk/issues/1566)
    @Nullable
    private CustomFields customFields(@Nullable final CustomFieldsDraft customFieldsDraft) {
        if (customFieldsDraft != null) {
            return new CustomFields() {
                @Override
                public Map<String, JsonNode> getFieldsJsonMap() {
                    return customFieldsDraft.getFields();
                }

                @Override
                @Nullable
                public JsonNode getFieldAsJsonNode(final String name) {
                    final Map<String, JsonNode> fields = customFieldsDraft.getFields();
                    return fields != null ? fields.get(name) : null;
                }

                @Override
                @Nullable
                public <T> T getField(final String name, final TypeReference<T> typeReference) {
                    try {
                        return Optional.ofNullable(getFieldAsJsonNode(name))
                                .map(jsonNode -> SphereJsonUtils.readObject(jsonNode, typeReference))
                                .orElse(null);
                    } catch (final RuntimeException e) {
                        throw new JsonException(e);
                    }
                }

                @Override
                @Nullable
                public String getFieldAsString(final String name) {
                    return getField(name, TypeReferences.stringTypeReference());
                }

                @Override
                @Nullable
                public Boolean getFieldAsBoolean(final String name) {
                    return getField(name, TypeReferences.booleanTypeReference());
                }

                @Override
                @Nullable
                public LocalizedString getFieldAsLocalizedString(final String name) {
                    return getField(name, LocalizedString.typeReference());
                }

                @Override
                @Nullable
                public String getFieldAsEnumKey(final String name) {
                    return getFieldAsString(name);
                }

                @Override
                @Nullable
                public String getFieldAsLocalizedEnumKey(final String name) {
                    return getFieldAsString(name);
                }

                @Override
                @Nullable
                public Integer getFieldAsInteger(final String name) {
                    return getField(name, TypeReferences.integerTypeReference());
                }

                @Override
                @Nullable
                public Long getFieldAsLong(final String name) {
                    return getField(name, TypeReferences.longTypeReference());
                }

                @Override
                @Nullable
                public Double getFieldAsDouble(final String name) {
                    return getField(name, TypeReferences.doubleTypeReference());
                }

                @Override
                @Nullable
                public MonetaryAmount getFieldAsMoney(final String name) {
                    return getField(name, TypeReferences.monetaryAmountTypeReference());
                }

                @Override
                @Nullable
                public LocalDate getFieldAsDate(final String name) {
                    return getField(name, TypeReferences.localDateTypeReference());
                }

                @Override
                @Nullable
                public ZonedDateTime getFieldAsDateTime(final String name) {
                    return getField(name, TypeReferences.zonedDateTimeTypeReference());
                }

                @Override
                @Nullable
                public LocalTime getFieldAsTime(final String name) {
                    return getField(name, TypeReferences.localTimeTypeReference());
                }

                @Override
                public Reference<Type> getType() {
                    final ResourceIdentifier<Type> type = customFieldsDraft.getType();
                    return Reference.ofResourceTypeIdAndId(type.getTypeId(), type.getId());
                }

                @Override
                public Set<String> getFieldAsStringSet(final String name) {
                    return getField(name, TypeReferences.stringSetTypeReference());
                }
            };
        }
        return null;
    }
}
