// 모듈화된 JS

let currentPage = 0;

function search() {
    let keyword = document.getElementById('keyword').value;
    let category = document.getElementById('bookCategoryId').value;

    if(keyword != '' & category != '')
        url = `/search/is1?keyword=${keyword}&bookCategoryName=${category}&page=0`
    else if(keyword == '' & category !='')
        url = `/search/is1?bookCategoryName=${category}&page=0`
    else if(keyword != '' & category =='')
        url = `/search/is1?keyword=${keyword}&page=0`
    else if(keyword == '' & category =='')
        url = "/search/is1?page=0"

    window.location.href = url;
}

// 페이지 로딩 시
$(document).ready(function() {
    // 현재 페이지 업데이트
    currentPage++;
    let loading = false;

    // 스크롤 이벤트 감지를 .index-contents에 적용
    $('.index-contents').scroll(function() {
        // 스크롤이 특정 위치에 도달하면 추가 데이터 로드
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight - 100 && !loading) {
            loading = true;

            // 현재 URL에서 keyword와 bookCategoryName 값을 가져오기
            let urlParams = new URLSearchParams(window.location.search);
            let keyword = urlParams.get('keyword');
            let category = urlParams.get('bookCategoryName');

            // URL 생성
            let url = '/search/loadMore?page=' + currentPage;

            // 검색어 및 카테고리 정보가 있다면 추가
            if (keyword) {
                url += '&keyword=' + keyword;
            }
            if (category) {
                url += '&bookCategoryName=' + category;
            }

            // 서버에 다음 페이지 데이터 요청
            $.ajax({
                type: 'GET',
                url: url,
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


    // 대출하기 버튼 클릭 시 이벤트 핸들러 설정
    $(document).on('click', '.rent-button', function () {
        const bookId = $(this).data('book-id');
        rentBook(bookId);
    });


    // 예약하기 버튼 클릭 시 이벤트 핸들러 설정
    $(document).on('click', '.reserve-button', function () {
        const bookId = $(this).data('book-id');
        reserveBook(bookId);
    });



    function rentBook(bookId) {
        $.ajax({
            url: `/api/books/${bookId}/rental`,
            type: "POST",
            success: function (response) {
                alert("대여가 완료되었습니다.");
                //현재 성능상 문제로 reload는 안함
                window.location.reload();
            },
            error: function (error) {
                alert("대여에 실패했습니다.");
                console.error(error);
                //추후 오류 문구 전달 필요?
            },
        });
    }

    function reserveBook(bookId) {
        $.ajax({
            url: `/api/books/${bookId}/reservation`,
            type: "POST",
            success: function (response) {
                alert("예약이 완료되었습니다.");
                //현재 성능상 문제로 reload는 안함
                window.location.reload();
            },
            error: function (error) {
                alert("예약에 실패했습니다.");
                console.error(error);
                //추후 오류 문구 전달 필요?
            },
        });
    }
});
