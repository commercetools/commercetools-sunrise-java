$(document).ready(function() {
    if (!Cookies.get('showInfo')) {
        $('#modalAnnounce').modal('show');
    }
});

$('#modalAnnounce').on('hidden.bs.modal', function (e) {
    Cookies.set('showInfo', 'false');
});