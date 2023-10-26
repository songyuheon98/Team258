$(document).ready(function () {
    $('#register-book-button').on('click', function () {
        var form = $('#register-book-form');

        var selectedYear = form.find('select[name="year"]').val();

        var data = {
            bookName: form.find('input[name="bookName"]').val(),
            bookAuthor: form.find('input[name="bookAuthor"]').val(),
            bookPublish: selectedYear, // 선택된 년도 값 사용
            bookCategoryId: form.find('select[name="bookCategoryId"] :selected').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/admin/books',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function (response) {
                alert('도서등록 성공!');
                window.location.reload();
            },
            error: function (xhr, status, error) {
                alert('도서등록 실패!');
                // 오류 메시지는 실제 API 응답에 따라 조절할 수 있습니다.
                // window.location.reload();
            }
        });
    });

    $('#manage-book-button').on('click', function () {
        window.location.href = '/admin/booksManage';
    })
    setDateSelectBox();

    function setDateSelectBox(){
        var now = new Date();
        var now_year = now.getFullYear();

        $("#year").append("<option value=''>Select Year</option>");

        // 2005년 부터 올해까지
        for(var i = now_year; i >= 1980; i--){
            $("#year").append("<option value='"+ i +"'>"+ i + " 년" +"</option>");
        }
    }
});
