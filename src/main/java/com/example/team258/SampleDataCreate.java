//package com.example.team258;
//
//import com.example.team258.common.entity.*;
//import com.example.team258.common.repository.BookRepository;
//import com.example.team258.common.repository.UserRepository;
//import com.example.team258.domain.admin.repository.BookCategoryRepository;
//import com.example.team258.domain.donation.entity.BookDonationEvent;
//import com.example.team258.domain.donation.repository.BookDonationEventRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//public class SampleDataCreate {
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
////        initService.userInit();
//        initService.bookInit();
////        initService.categoryInit();
////        initService.bookEventDonationInit();
//    }
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//
//        private final UserRepository userRepository;
//        private final BookRepository bookRepository;
//        private final BookCategoryRepository bookCategoryRepository;
//        private final BookDonationEventRepository bookDonationEventRepository;
//        private final PasswordEncoder passwordEncoder;
//
//        public void userInit() {
//
//            for (int i = 0; i < 100; i++) {
//                User user = User.builder().username("bin000"+i+i).password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.USER).build();
//                userRepository.save(user);
//            }
//        }
//        public void bookInit() {
//            for (int i = 0; i < 100; i++) {
//                Book book = Book.builder().bookName("book"+i).bookAuthor("author"+i).bookPublish("2011").bookStatus(BookStatusEnum.POSSIBLE).build();
//                bookRepository.save(book);
//            }
//        }
//        public void categoryInit() {
//            for (int i = 0; i < 100; i++) {
//                BookCategory bookCategory = BookCategory.builder().bookCategoryName("category"+i).build();
//                bookCategoryRepository.save(bookCategory);
//            }
//        }
//
//        public void bookEventDonationInit(){
//            for (int i = 0; i < 100; i++) {
//                BookDonationEvent bookDonationEvent= BookDonationEvent.builder().createdAt(LocalDateTime.parse("2023-10-12T19:16:59")).closedAt(LocalDateTime.parse("2024-10-12T19:16:59")).build();
//                bookDonationEventRepository.save(bookDonationEvent);
//            }
//        }
//
//
//    }
//}