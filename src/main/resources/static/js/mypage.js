$(document).ready(function() {
    $('#modifyUser-button').on('click', function() {
        var form = $('#update-form');
        var data = {
            password1: form.find('input[name="password1"]').val(),
            password2: form.find('input[name="password2"]').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/users/update',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('비밀번호 정보 수정 성공!');
                window.location.href = "/";
            },
            error: function(xhr, status, error) {
                alert('비밀번호 수정 실패!'); // 오류 메시지는 실제 API 응답에 따라 조절할 수 있습니다.
                window.location.href = "/mypage";
            }
        });
    });

});
$(document).ready(function () {
    $('#escape-button').on('click', function () {
        $.ajax({
            type: 'DELETE',
            url: '/api/users/escape',
            success: function () {
                alert('탈퇴가 완료되었습니다.');
                window.location.href = '/';
            },
            error: function () {
                alert('탈퇴에 실패하였습니다.');
                window.location.href = '/escape';
            }
        });
    });

    $('#main-page-button').on('click', function () {
        window.location.href = '/main';
    });
});
