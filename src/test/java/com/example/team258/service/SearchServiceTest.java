package com.example.team258.service;

import com.example.team258.common.entity.QBook;
import com.example.team258.domain.admin.dto.AdminBooksRequestDto;
import com.example.team258.domain.bookSearch.service.SearchService;
import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.example.team258.common.repository.BookRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    public void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        for (int i = 0; i < 40; i++) {
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookAuthor("작가"+i)
                    .bookPublish("2011")
                    .bookCategoryId(1L)
                    .bookName("제목"+i)
                    .bookStatus(BookStatusEnum.POSSIBLE)
                    .build();
            books.add(new Book(requestDto,bookCategory));
        }
        Page<Book> bookPage = new PageImpl<>(books);

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooks(0);
        for(int i = 0;i<20;i++){
            int t = i+20;
            assertEquals("작가"+t, result.getContent().get(t).getBookAuthor());
            assertEquals("제목"+t, result.getContent().get(t).getBookName());
        }
    }

    @Test
    public void testGetOneBooks() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        for (int i = 0; i < 20; i++) {
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookAuthor("작가"+i)
                    .bookPublish("2011")
                    .bookCategoryId(1L)
                    .bookName("제목"+i)
                    .bookStatus(BookStatusEnum.POSSIBLE)
                    .build();
            books.add(new Book(requestDto,bookCategory));
        }
        when(bookRepository.findById(3L)).thenReturn(Optional.ofNullable(books.get(3)));

        BookResponseDto result = searchService.getBookById(3L);
        assertEquals("작가3",result.getBookAuthor());
        assertEquals("제목3",result.getBookName());
    }

    @Test
    public void testGetBooksWithCategory() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .childCategories(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();


        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);
        List<BookCategory> bookCategories = new ArrayList<>();
        bookCategories.add(bookCategory);
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        when(bookCategoryRepository.findByBookCategoryName("과학")).thenReturn(bookCategory);
        when(bookRepository.findAllByCategories(bookCategories,pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooksByCategory("과학",0);
        assertEquals("과학책", result.getContent().get(0).getBookName());
        assertEquals("과학작가", result.getContent().get(0).getBookAuthor());
    }

    @Test
    public void testGetBooksWithKeyword() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .childCategories(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();


        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        when(bookRepository.findAllByBookNameContaining("과학",pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooksByKeyword("과학",0);
        assertEquals("과학책", result.getContent().get(0).getBookName());
        assertEquals("과학작가", result.getContent().get(0).getBookAuthor());
    }

    @Test
    public void testGetBooksWithCategoryAndKeyword() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .childCategories(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();


        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);
        List<BookCategory> bookCategories = new ArrayList<>();
        bookCategories.add(bookCategory);
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        when(bookCategoryRepository.findByBookCategoryName("과학")).thenReturn(bookCategory);
        when(bookRepository.findAllByCategoriesAndBookNameContaining(bookCategories,"학책",pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooksByCategoryOrKeyword("과학","학책",0);
        assertEquals("과학책", result.getContent().get(0).getBookName());
        assertEquals("과학작가", result.getContent().get(0).getBookAuthor());
    }


    @Test
    public void testGetAllBooksV3() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        for (int i = 0; i < 40; i++) {
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookAuthor("작가"+i)
                    .bookPublish("2011")
                    .bookCategoryId(1L)
                    .bookName("제목"+i)
                    .bookStatus(BookStatusEnum.POSSIBLE)
                    .build();
            books.add(new Book(requestDto,bookCategory));
        }
        Page<Book> bookPage = new PageImpl<>(books);

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        when(bookRepository.findAll(builder,pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooksByCategoryOrKeyword3(null,null,0);
        for(int i = 0;i<20;i++){
            int t = i+20;
            assertEquals("작가"+t, result.getContent().get(t).getBookAuthor());
            assertEquals("제목"+t, result.getContent().get(t).getBookName());
        }
    }



    @Test
    public void testGetBooksWithCategoryV3() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .childCategories(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();


        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);
        List<BookCategory> bookCategories = new ArrayList<>();
        bookCategories.add(bookCategory);
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBook.bookCategory.in(bookCategories));

        when(bookCategoryRepository.findByBookCategoryName("과학")).thenReturn(bookCategory);
        when(bookRepository.findAll(builder,pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooksByCategoryOrKeyword3("과학",null,0);
        assertEquals("과학책", result.getContent().get(0).getBookName());
        assertEquals("과학작가", result.getContent().get(0).getBookAuthor());
    }

    @Test
    public void testGetBooksWithKeywordV3() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .childCategories(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();


        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBook.bookName.contains("과학"));

        when(bookRepository.findAll(builder,pageable)).thenReturn(bookPage);

        Page<BookResponseDto> result = searchService.getAllBooksByCategoryOrKeyword3(null,"과학",0);
        assertEquals("과학책", result.getContent().get(0).getBookName());
        assertEquals("과학작가", result.getContent().get(0).getBookAuthor());
    }

    @Test
    public void testGetBooksWithCategoryAndKeywordV3() {
        List<Book> books = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .childCategories(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();


        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);
        List<BookCategory> bookCategories = new ArrayList<>();
        bookCategories.add(bookCategory);
        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(0,20,sort);

        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBook.bookName.contains("학책"));
        builder.and(qBook.bookCategory.in(bookCategories));

        when(bookRepository.findAll(builder,pageable)).thenReturn(bookPage);

        when(bookCategoryRepository.findByBookCategoryName("과학")).thenReturn(bookCategory);

        Page<BookResponseDto> result = searchService.getAllBooksByCategoryOrKeyword3("과학","학책",0);
        assertEquals("과학책", result.getContent().get(0).getBookName());
        assertEquals("과학작가", result.getContent().get(0).getBookAuthor());
    }

}