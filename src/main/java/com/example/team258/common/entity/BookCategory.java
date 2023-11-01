package com.example.team258.common.entity;

import com.example.team258.domain.admin.dto.AdminCategoriesRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "book_category")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_category_id")
    private Long bookCategoryId;

    @Column(name="book_category_isbn_code")
    private Long bookCategoryIsbnCode;

    @Column(name="book_category_name")
    private String bookCategoryName;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookCategory")
    private List<Book> books = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "book_category_id", insertable = false, updatable = false)
    private BookCategory parentCategory;

    @OneToMany(orphanRemoval = true, mappedBy = "parentCategory")
    private List<BookCategory> childCategories;


    public BookCategory(AdminCategoriesRequestDto requestDto) {
        this.bookCategoryName = requestDto.getBookCategoryName();
        this.bookCategoryIsbnCode = requestDto.getBookCategoryIsbnCode();
    }

    public void addChildCategory(BookCategory childCategory) {
        this.childCategories.add(childCategory);
        childCategory.setParentCategory(this);
    }

    private void setParentCategory(BookCategory bookCategory) {
        this.parentCategory = bookCategory;
    }

    public void updateBookCategory(String newBookCategoryName, Long newBookCategoryIsbnCode) {
        this.bookCategoryName = newBookCategoryName;
        this.bookCategoryIsbnCode = newBookCategoryIsbnCode;
    }
}