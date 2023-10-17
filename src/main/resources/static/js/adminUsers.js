$(document).ready(function () {
    window.deleteUser = function(userId) {
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/users/' + userId,
            success: function (response) {
                alert('사용자 삭제가 완료되었습니다.');
                window.location.href = '/admin/users';
            },
            error: function (error) {
                alert('사용자 삭제에 실패하였습니다.');
                window.location.href = '/admin/users';
            }
        });
    };
});

