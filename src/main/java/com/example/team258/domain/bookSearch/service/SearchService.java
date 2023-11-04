package com.example.team258.domain.bookSearch.service;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.QBook;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.CustomBookRepository;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final CustomBookRepository customBookRepository;


    public Page<BookResponseDto> getAllBooks(int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");
        Pageable pageable = PageRequest.of(page,20,sort);

        Page<Book> bookList = bookRepository.findAll(pageable);
        return bookList.map(BookResponseDto::new);
    }

    public BookResponseDto getBookById(Long bookId) {
        Book book =bookRepository.findById(bookId).orElseThrow(()->new NullPointerException("오류"));
        return new BookResponseDto(book);
    }

    public Page<BookResponseDto> getAllBooksByCategory(String bookCategoryName,int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");
        Pageable pageable = PageRequest.of(page,20,sort);

        BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
        List<BookCategory> bookCategories = saveAllCategories(bookCategory);

        Page<Book> bookList = bookRepository.findAllByCategories(bookCategories,pageable);

        return bookList.map(BookResponseDto::new);
    }

    public Page<BookResponseDto> getAllBooksByKeyword(String keyword, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");

        Pageable pageable = PageRequest.of(page,20,sort);

        Page<Book> bookList = bookRepository.findAllByBookNameContaining(keyword, pageable);
        return bookList.map(BookResponseDto::new);
    }

    public Page<BookResponseDto> getAllBooksByCategoryOrKeyword(String bookCategoryName, String keyword, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");
        Pageable pageable = PageRequest.of(page,20,sort);

        BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
        List<BookCategory> bookCategories = saveAllCategories(bookCategory);

        Page<Book> bookList = bookRepository.findAllByCategoriesAndBookNameContaining(bookCategories, keyword, pageable);
        return bookList.map(BookResponseDto::new);
    }

    //queryDsl 사용
    @Transactional(readOnly = true)
    public Page<BookResponseDto> getAllBooksByCategoryOrKeyword2(String bookCategoryName, String keyword, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(page, 20, sort);

        List<BookCategory> bookCategories = null;
        if (bookCategoryName != null) {
            BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
            bookCategories = saveAllCategories(bookCategory);
        }

        Page<Book> bookList = bookRepository.findAllByCategoriesAndBookNameContaining2(bookCategories, keyword, pageable);

        return bookList.map(BookResponseDto::new);

    }

    public Page<BookResponseDto> getAllBooksByCategoryOrKeyword3(String bookCategoryName, String keyword, int page) {
        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        List<BookCategory> bookCategories = null;
        if (bookCategoryName != null) {
            BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
            bookCategories = saveAllCategories(bookCategory);
        }
        if(keyword != null)
            builder.and(qBook.bookName.contains(keyword));
        if(bookCategories != null)
            builder.and(qBook.bookCategory.in(bookCategories));

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(page, 20, sort);

        Page<BookResponseDto> bookList = bookRepository.findAll(builder, pageable).map(BookResponseDto::new);
        System.out.println(bookList.getTotalElements());
        return bookList;
    }

    private List<BookCategory> saveAllCategories(BookCategory bookCategory){
        BookCategory category = bookCategory;
        List<BookCategory> answer = new ArrayList<>();
        answer.add(category);
        if (category.getChildCategories().isEmpty()) return answer;
        for(BookCategory tmp : category.getChildCategories()){
            answer.addAll(saveAllCategories(tmp));
        }
        return answer;
    }

    public Slice<BookResponseDto> getAllBooksV2(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Book> booksSlice = bookRepository.findAll(pageable);
        List<BookResponseDto> bookResponseDtos = booksSlice.map(BookResponseDto::new).getContent();
        return new SliceImpl<>(bookResponseDtos, pageable, booksSlice.hasNext());
    }

    public Slice<BookResponseDto> getAllBooksByCategoryOrKeywordV4(String bookCategoryName, String keyword, int page) {
        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        List<BookCategory> bookCategories = null;

        if (bookCategoryName != null) {
            BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
            bookCategories = saveAllCategories(bookCategory);
        }

        if (keyword != null)
            builder.and(qBook.bookName.contains(keyword));
        if (bookCategories != null)
            builder.and(qBook.bookCategory.in(bookCategories));

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(page, 20, sort);

        // Slice로 변경
        Slice<BookResponseDto> bookList = customBookRepository.findAllSliceBooks(builder, pageable).map(BookResponseDto::new);
        System.out.println(bookList.hasNext());
        return bookList;
    }

    //더보기용 서비스
    public Slice<BookResponseDto> getMoreBooksByCategoryOrKeyword(String bookCategoryName, String keyword, int page) {
        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        List<BookCategory> bookCategories = new ArrayList<>(); // 초기화

        if (bookCategoryName != null) {
            BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
            if (bookCategory != null) { // null 체크 추가
                bookCategories = saveAllCategories(bookCategory);
            }
        }

        if (keyword != null)
            builder.and(qBook.bookName.contains(keyword));
        if (!bookCategories.isEmpty()) // 리스트가 비어있는지 확인
            builder.and(qBook.bookCategory.in(bookCategories));

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(page, 20, sort);

        // Slice로 추가 로드 데이터 가져오기
        Slice<BookResponseDto> bookList = customBookRepository.findAllSliceBooks(builder, pageable).map(BookResponseDto::new);
        System.out.println(bookList.hasNext());

        return bookList;
    }

}
