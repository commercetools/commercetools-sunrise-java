
// Submit faceted search on change
$("form#form-filter-products, form#form-filter-products-mobile").change(function() {
    this.submit();
});

// Submit language switcher on change
$("form#form-select-language, form#form-select-language-mobile").change(function() {
    this.submit();
});

// Submit country switcher on change
$("form#form-select-country, form#form-select-country-mobile").change(function() {
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

// Initializes editable data
function openForm(formClassName) {
    $("." + formClassName + "-hide").hide();
    $("." + formClassName + "-show").show();
}

function closeForm(formClassName) {
    $("." + formClassName + "-hide").show();
    $("." + formClassName + "-show").hide();
}

function initializeEditableData(formClassName) {
   var formWrapper = $(formClassName);
   if (formWrapper.hasClass("in")) {
       openForm(formClassName);
   } else {
       closeForm(formClassName);
   }
   $("." + formClassName + "-show-btn").click(function(){ openForm(formClassName); });
   $("." + formClassName + "-hide-btn").click(function(){ closeForm(formClassName); });
}

initializeEditableData("personal-details-edit");

//$("img.pop-product-image").error(function(){
//    $(this).attr('src', '/assets/public/images/empty-pop.jpg');
//});
//
//$("img.bzoom_thumb_image").error(function(){
//    $(this).attr('src', '/assets/public/images/empty-pdp.jpg');
//    $(".bzoom_thumb.bzoom_thumb_active").unbind();
//});
