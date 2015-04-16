// Close hamburger menu on click

$(function () {
	$('.navbar-collapse ul li a:not(.dropdown-toggle)').bind('click touchstart', function () {
	    $('.navbar-toggle:visible').click();
	});
});