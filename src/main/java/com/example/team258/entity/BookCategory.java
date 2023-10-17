package com.example.team258.entity;

import com.example.team258.dto.AdminCategoriesRequestDto;
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

    @Column(name="book_category_name")
    private String bookCategoryName;

    @Builder.Default
    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "bookCategory")
    private List<Book> books = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private BookCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<BookCategory> childCategories;


    public BookCategory(AdminCategoriesRequestDto requestDto) {
        this.bookCategoryName = requestDto.getBookCategoryName();
    }

    public void addChildCategory(BookCategory childCategory) {
        this.childCategories.add(childCategory);
        childCategory.setParentCategory(this);
    }

    private void setParentCategory(BookCategory bookCategory) {
        this.parentCategory = bookCategory;
    }

    public void changeBookCategoryName(String newBookCategoryName) {
        this.bookCategoryName = newBookCategoryName;
    }
}

/*
성능적 차이가 발생할 여지가 있다.

카테고리를 자기 자신을 참조하는 방법:
간단한 쿼리: 데이터가 단일 테이블에 있으므로 간단하고 직관적인 쿼리로 데이터를 가져올 수 있습니다.
계층 구조 표현 용이: 상위 카테고리와 하위 카테고리 간의 계층 구조를 표현하는 데 용이합니다.

카테고리 클래스를 두 개로 나누는 방법:
데이터 정규화: 각 카테고리 클래스가 자체적으로 명확한 의미를 가지므로 데이터를 정규화할 수 있습니다.
각 카테고리에 맞는 속성 저장: 각 카테고리 클래스가 필요한 속성을 갖고 있어 데이터의 의미가 명확합니다.
*/