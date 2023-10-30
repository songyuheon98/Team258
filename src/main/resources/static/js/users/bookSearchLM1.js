// 모듈화된 JS

let currentPage = 0;

// 페이지 로딩 시
$(document).ready(function() {
    window.goToNextPage = function() {
        // 서버에 다음 페이지 데이터 요청
        $.ajax({
            type: 'GET',
            url: '/search/lm1',
            data: {
                page: currentPage + 1 // 다음 페이지로 이동
            },
            success: function(data) {
                console.log(data);

                // content가 비어있는지 확인
                if (data.content && data.content.length > 0) {
                    // 받은 JSON 데이터를 가지고 동적으로 HTML을 생성하여 추가
                    data.content.forEach(book => {
                        let bookHtml = `
                            <tr>
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
                        $('#test').append(bookHtml);
                    });

                    // 현재 페이지 업데이트
                    currentPage++;

                    // 서버에서 받은 다음 페이지 값으로 업데이트
                    currentPageThymeleaf = currentPage;
                } else {
                    // 더 이상 데이터가 없을 때의 처리
                    console.log("No more data");
                }
            },
            error: function(xhr, status, error) {
                // 오류 처리 로직
                console.error(error);
            }
        });
    }

// 테스트 버튼을 클릭할 때 실행되는 함수
    window.testButton = function() {
        const newBook = {
            bookName: "New Book",
            bookAuthor: "New Author",
            bookPublish: "2023",
            bookStatus: "Available",
            bookId: "new-book-id"
        };

        let newBookHtml = `
      <tr>
        <td><input class="form-control" type="text" value="${newBook.bookName}" name="userId" readonly/></td>
        <td><input class="form-control" type="text" value="${newBook.bookAuthor}" name="username" readonly/></td>
        <td><input class="form-control" type="text" value="${newBook.bookPublish}" name="role" readonly/></td>
        <td><input class="form-control" type="text" value="${newBook.bookStatus}" name="role" readonly/></td>
        <td>
          <button type="button" class="btn btn-success rent-button" data-book-id="${newBook.bookId}">대출하기</button>
          <button type="button" class="btn btn-primary reserve-button" data-book-id="${newBook.bookId}">예약하기</button>
        </td>
      </tr>`;

        $('#test').append(newBookHtml);
    }

    // 테스트 버튼 클릭 시 이벤트 핸들러 설정
    $(document).on('click', '#test-button', testButton);
});