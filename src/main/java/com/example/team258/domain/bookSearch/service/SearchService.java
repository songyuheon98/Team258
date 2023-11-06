package com.example.team258.domain.bookSearch.service;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.QBook;
import com.example.team258.common.repository.CustomBookRepository;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.example.team258.common.repository.BookRepository;
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

    public Slice<BookResponseDto> getAllBooksByCategoryOrKeywordFTI(String bookCategoryName, String keyword, int page) {

        List<BookCategory> bookCategories = null;
        if (bookCategoryName != null) {
            BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
            bookCategories = saveAllCategories(bookCategory);
        }
        Slice<BookResponseDto> bookList = new SliceImpl<>(new ArrayList<>());
        Sort sort = Sort.by(Sort.Direction.ASC, "book_id");
        Pageable pageable = PageRequest.of(page, 20, sort);
        if (keyword != null){
            String[] keywords = keyword.split(" ");
            String tmp = "+"+keywords[0];
            if(keywords.length>1){
                for(int i = 1;i<keywords.length;i++){
                    tmp=tmp+" +"+ keywords[i];
                }
            }
            if(bookCategories != null){
                bookList = bookRepository.findAllByCategoriesAndBookNameContainingFTI(pageable,bookCategories,tmp).map(BookResponseDto::new);
            } else{
                bookList = bookRepository.findAllByBookNameContainingFTI(pageable,tmp).map(BookResponseDto::new);
            }
        } else if (keyword == null){
            if(bookCategories != null){
                bookList = bookRepository.findAllByCategories(bookCategories,pageable).map(BookResponseDto::new);
            } else {
                bookList = bookRepository.findAll(pageable).map(BookResponseDto::new);
            }
        }
        // Slice로 변경
        return bookList;
    }
}
