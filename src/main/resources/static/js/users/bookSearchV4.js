$(document).ready(function() {
    window.goToNextPage=function () {
        let nextPage = currentPage + 1;
        window.location.href = '/search/v4?page=' + nextPage;
    }
    window.goToPrePage=function () {
        let nextPage = currentPage - 1;
        window.location.href = '/search/v4?page=' + nextPage;
    }
});