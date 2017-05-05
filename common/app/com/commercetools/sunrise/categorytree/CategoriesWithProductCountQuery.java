package com.commercetools.sunrise.categorytree;

import com.commercetools.sunrise.ctp.graphql.SingleGraphQLRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.sphere.sdk.expansion.ExpansionPath;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryDsl;
import io.sphere.sdk.queries.QueryPredicate;
import io.sphere.sdk.queries.QuerySort;
import play.libs.Json;

import javax.annotation.Nullable;
import java.util.List;

import static com.commercetools.sunrise.ctp.graphql.GraphQLUtils.readFromResource;
import static io.sphere.sdk.utils.SphereInternalUtils.listOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public final class CategoriesWithProductCountQuery extends SingleGraphQLRequest<PagedQueryResult<CategoryWithProductCount>> implements QueryDsl<CategoryWithProductCount, CategoriesWithProductCountQuery> {

    private static final String QUERY = readFromResource("CategoriesWithProductCount.graphql");

    @Nullable
    private final Long limit;
    @Nullable
    private final Long offset;
    private final List<QuerySort<CategoryWithProductCount>> sort;
    private final List<QueryPredicate<CategoryWithProductCount>> predicates;

    private CategoriesWithProductCountQuery(@Nullable final Long limit, @Nullable final Long offset,
                                            final List<QuerySort<CategoryWithProductCount>> sort,
                                            final List<QueryPredicate<CategoryWithProductCount>> predicates) {
        super(resultTypeReference(), "categories");
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
        this.predicates = predicates;
    }

    public static CategoriesWithProductCountQuery of() {
        return new CategoriesWithProductCountQuery(null, null, emptyList(), emptyList());
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Nullable
    @Override
    public JsonNode getVariables() {
        final ObjectNode objectNode = Json.newObject();
        objectNode.put("limit", limit);
        objectNode.put("offset", offset);
        final ArrayNode sortArrayNode = Json.newArray();
        sort.forEach(querySort -> sortArrayNode.add(querySort.toSphereSort()));
        objectNode.set("sort", sortArrayNode);
        predicates.stream()
                .reduce(QueryPredicate::and)
                .ifPresent(joinedPredicates -> objectNode.put("where", joinedPredicates.toSphereQuery()));
        return objectNode;
    }

    @Nullable
    @Override
    public Long limit() {
        return limit;
    }

    @Nullable
    @Override
    public Long offset() {
        return offset;
    }

    @Override
    public List<QuerySort<CategoryWithProductCount>> sort() {
        return sort;
    }

    @Override
    public List<QueryPredicate<CategoryWithProductCount>> predicates() {
        return predicates;
    }

    @Override
    public CategoriesWithProductCountQuery withLimit(final Long limit) {
        return new CategoriesWithProductCountQuery(limit, offset(), sort(), predicates());
    }

    @Override
    public CategoriesWithProductCountQuery withOffset(final Long offset) {
        return new CategoriesWithProductCountQuery(limit(), offset, sort(), predicates());
    }

    @Override
    public CategoriesWithProductCountQuery withSort(final List<QuerySort<CategoryWithProductCount>> sort) {
        return new CategoriesWithProductCountQuery(limit(), offset(), sort, predicates());
    }

    @Override
    public CategoriesWithProductCountQuery withSort(final QuerySort<CategoryWithProductCount> sort) {
        return withSort(singletonList(sort));
    }

    @Override
    public CategoriesWithProductCountQuery plusSort(final List<QuerySort<CategoryWithProductCount>> sort) {
        return withSort(listOf(sort(), sort));
    }

    @Override
    public CategoriesWithProductCountQuery plusSort(final QuerySort<CategoryWithProductCount> sort) {
        return withSort(listOf(sort(), sort));
    }

    @Override
    public CategoriesWithProductCountQuery withPredicates(final List<QueryPredicate<CategoryWithProductCount>> queryPredicates) {
        return new CategoriesWithProductCountQuery(limit(), offset(), sort(), queryPredicates);
    }

    @Override
    public CategoriesWithProductCountQuery withPredicates(final QueryPredicate<CategoryWithProductCount> predicate) {
        return withPredicates(singletonList(predicate));
    }

    @Override
    public CategoriesWithProductCountQuery plusPredicates(final List<QueryPredicate<CategoryWithProductCount>> queryPredicates) {
        return withPredicates(listOf(predicates(), queryPredicates));
    }

    @Override
    public CategoriesWithProductCountQuery plusPredicates(final QueryPredicate<CategoryWithProductCount> predicate) {
        return withPredicates(listOf(predicates(), predicate));
    }

    @Override
    public CategoriesWithProductCountQuery withExpansionPaths(final List<ExpansionPath<CategoryWithProductCount>> expansionPaths) {
        return this; // not applicable
    }

    @Override
    public CategoriesWithProductCountQuery withExpansionPaths(final ExpansionPath<CategoryWithProductCount> expansionPath) {
        return this; // not applicable
    }

    @Override
    public CategoriesWithProductCountQuery plusExpansionPaths(final List<ExpansionPath<CategoryWithProductCount>> expansionPaths) {
        return this; // not applicable
    }

    @Override
    public CategoriesWithProductCountQuery plusExpansionPaths(final ExpansionPath<CategoryWithProductCount> expansionPath) {
        return this; // not applicable
    }

    @Override
    public List<ExpansionPath<CategoryWithProductCount>> expansionPaths() {
        return emptyList(); // not applicable
    }

    @Override
    public CategoriesWithProductCountQuery withFetchTotal(final boolean fetchTotal) {
        return this; // not applicable
    }

    @Nullable
    @Override
    public Boolean fetchTotal() {
        return false; // not applicable
    }

    @Override
    public String endpoint() {
        return "/graphql"; // not used
    }

    public static TypeReference<PagedQueryResult<CategoryWithProductCount>> resultTypeReference() {
        return new TypeReference<PagedQueryResult<CategoryWithProductCount>>() {
            @Override
            public String toString() {
                return "TypeReference<PagedQueryResult<CategoryWithProductCount>>";
            }
        };
    }
}
