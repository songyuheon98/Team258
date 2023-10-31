$(document).ready(function() {
    let currentPage = 0;
    let loading = false;

    // 스크롤 이벤트 감지를 .index-contents에 적용
    $('.index-contents').scroll(function() {
        // 스크롤이 특정 위치에 도달하면 추가 데이터 로드
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight - 100 && !loading) {
            loading = true;

            // 서버에 다음 페이지 데이터 요청
            $.ajax({
                type: 'GET',
                url: '/search/loadMore',
                data: {
                    bookCategoryName: $('#bookCategoryId').val(),
                    keyword: $('#keyword').val(),
                    page: currentPage + 1
                },
                dataType: 'json',

                success: function(data) {
                    console.log(data);

                    // books 배열에 접근하도록 수정
                    data[0].bookResponseDtos.forEach(book => {
                        let bookHtml = `<tr>
                                            <td><input class="form-control" type="text" value="${book.bookName}" name="userId" readonly/></td>
                                            <td><input class="form-control" type="text" value="${book.bookAuthor}" name="username" readonly/></td>
                                            <td><input class="form-control" type="text" value="${book.bookPublish}" name="role" readonly/></td>
                                            <td><input class="form-control" type="text" value="${book.bookStatus}" name="role" readonly/></td>
                                            <td>
                                                <button type="button" class="btn btn-success rent-button" data-book-id="${book.bookId}">대출하기</button>
                                                <button type="button" class="btn btn-primary reserve-button" data-book-id="${book.bookId}">예약하기</button>
                                            </td>
                                        </tr>`;

                        // 동적으로 생성된 HTML을 현재 테이블에 추가
                        $('#load-more-test').append(bookHtml);
                    });

                    // 현재 페이지 업데이트
                    currentPage++;

                    loading = false; // 로딩 상태 해제
                },
                error: function(xhr, status, error) {
                    // 오류 처리 로직
                    console.error(error);
                    loading = false; // 로딩 상태 해제
                }
            });
        }
    });
});
