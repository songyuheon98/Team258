// 모듈화된 JS

let currentPage = 0;

// 페이지 로딩 시
$(document).ready(function() {
    window.goToNextPage = function() {
        // 서버에 다음 페이지 데이터 요청
        $.ajax({
            type: 'GET',
            url: '/search/loadMore', // 엔드포인트 변경
            data: {
                bookCategoryName: $('#bookCategoryId').val(),
                keyword: $('#keyword').val(),
                page: currentPage + 1 // 다음 페이지로 이동
            },
            dataType: 'json', // 추가

            success: function(data) {
                console.log(data);

                // books 배열에 접근하도록 수정
                data[0].bookResponseDtos.forEach(book => {
                    let bookHtml =  `<tr>
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
                currentPageThymeleaf++;

                // 서버에서 받은 다음 페이지 값으로 업데이트
                currentPage = currentPageThymeleaf;
            },
            error: function(xhr, status, error) {
                // 오류 처리 로직
                console.error(error);
            }
        });
    }
});
