$(document).ready(function() {

    window.bookDonationApply = function(bookId) {
        var applyDate = new Date().toISOString(); // 현재 날짜 및 시간을 ISO 형식으로 가져옵니다.

        var donationId = $('#donationId').val(); // 숨겨진 input 필드에서 donationId 값을 가져옵니다.

        var data = {
            donationId: donationId,
            bookId: bookId,
            applyDate: applyDate
        };

        $.ajax({
            type: 'POST',
            url: '/api/user/bookApplyDonation',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert(response.msg);
                location.reload(); // 페이지를 다시 로드하여 최신 정보를 표시합니다.
            },
            error: function(xhr, status, error) {
                alert('나눔 신청 실패!');
            }
        });
    };

});
