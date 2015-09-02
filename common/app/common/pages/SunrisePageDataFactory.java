package common.pages;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.i18n.Messages;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class SunrisePageDataFactory {
    private final Messages messages;
    private final UserContext userContext;
    private final ProjectContext projectContext;
    private final CategoryTree categoryTree;
    private ReverseRouter reverseRouter;
    private final Optional<String> saleCategoryExternalId;

    public SunrisePageDataFactory(final Messages messages, final UserContext userContext, final ProjectContext projectContext,
                                  final CategoryTree categoryTree, final ReverseRouter reverseRouter,
                                  @Nullable final String saleCategoryExternalId) {
        this.messages = messages;
        this.userContext = userContext;
        this.projectContext = projectContext;
        this.categoryTree = categoryTree;
        this.reverseRouter = reverseRouter;
        this.saleCategoryExternalId = Optional.ofNullable(saleCategoryExternalId);
    }

    public SunrisePageData create(final PageContent pageContent) {
        final PageHeader pageHeader = getPageHeader(pageContent);
        final PageFooter pageFooter = getPageFooter();
        final SeoData seoData = getSeoData();
        final PageMeta pageMeta = getPageMeta();
        return new SunrisePageData(pageHeader, pageFooter, pageContent, seoData, pageMeta);
    }

    private PageHeader getPageHeader(final PageContent pageContent) {
        final String title = messages.at("header.title") + pageContent.additionalTitle();
        final CollectionData<SelectableData> countries = getCountries();
        final NavMenuData navMenuData = getNavMenuData();
        return new PageHeader(messages, title, countries, navMenuData);
    }

    private NavMenuData getNavMenuData() {
        final List<CategoryData> categoryDataList = new ArrayList<>();
        categoryTree.getRoots().forEach(root -> categoryDataList.add(getCategoryData(root, null)));
        return new NavMenuData(categoryDataList);
    }

    private CategoryData getCategoryData(final Category category, @Nullable String rootSlug) {
        final String name = category.getName().find(userContext.locales()).orElse("");
        final String slug = category.getSlug().find(userContext.locale()).orElse("");
        final String language = userContext.locale().toLanguageTag();
        final String url;
        if (rootSlug != null) {
            url = reverseRouter.subCategory(language, rootSlug, slug, 1).url();
        } else {
            url = reverseRouter.category(language, slug, 1).url();
        }
        final List<CategoryData> childrenCategoryData = categoryTree.findChildren(category).stream()
                .map(child -> getCategoryData(child, Optional.ofNullable(rootSlug).orElse(slug)))
                .collect(toList());
        final Optional<String> externalId = Optional.ofNullable(category.getExternalId());
        final boolean isSale = externalId.isPresent() && externalId.equals(saleCategoryExternalId);
        return new CategoryData(name, url, childrenCategoryData, isSale);
    }

    private CollectionData<SelectableData> getCountries() {
        final List<SelectableData> countries = projectContext.countries().stream()
                .map(country -> {
                    final String name = country.getName();
                    final String value = country.getAlpha2();
                    final String currency = country.getCurrency().getCurrencyCode();
                    final String image = String.format("assets/img/flags/%s.svg", value.toLowerCase());
                    return new SelectableData(name, value, currency, image, false);
                }).collect(toList());
        return new CollectionData<>(messages.at("header.countries"), countries);
    }

    private PageFooter getPageFooter() {
        return new PageFooter();
    }

    private SeoData getSeoData() {
        return new SeoData();
    }

    private PageMeta getPageMeta() {
        return new PageMeta();
    }
}
