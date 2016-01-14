
// Submit faceted search on change
$("form#form-filter-products, form#form-filter-products-mobile").change(function() {
    this.submit();
});

// Clear all selected facet options when click on "clear"
$("form#form-filter-products .facet-clear-btn, form#form-filter-products-mobile .facet-clear-btn").click(function(e) {
    e.stopPropagation();
    var attributeName = $(this).data("target");
    var input = $("input[name=" + attributeName + "]");
    input.prop("checked", false);
    input.closest("form").submit();
});

$("form#form-select-language, form#form-select-language-mobile").change(function() {
    this.submit();
});

//function changeVariant(select) {
//    var selectedOption = select.options[select.selectedIndex];
//    if (selectedOption.value) {
//        window.location = selectedOption.value;
//    }
//}



//$("img.pop-product-image").error(function(){
//    $(this).attr('src', '/assets/public/images/empty-pop.jpg');
//});
//
//$("img.bzoom_thumb_image").error(function(){
//    $(this).attr('src', '/assets/public/images/empty-pdp.jpg');
//    $(".bzoom_thumb.bzoom_thumb_active").unbind();
//});
