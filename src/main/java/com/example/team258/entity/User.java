package com.example.team258.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User extends Timestamped{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Builder.Default
    @OneToMany(orphanRemoval = true,mappedBy = "user")
    private List<BookReservation> bookReservations = new ArrayList<>();

    @Builder.Default
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<BookRent> bookRents = new ArrayList<>();

    @Builder.Default
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<BookApplyDonation> bookApplyDonations = new ArrayList<>();


    public User(String username, String password, UserRoleEnum role) {
        this.username=username;
        this.password=password;
        this.role=role;
    }

    public void addBookRent(BookRent bookRent) {
        this.bookRents.add(bookRent);
    }

    public void addBookReservation(BookReservation bookReservation) {
        this.bookReservations.add(bookReservation);
    }
    //public void addBookReservation(BookReservation bookReservation){
    //    this.bookReservations.add(bookReservation);
    //    bookReservation.addUser(this);
    //}

    public void addBookApplyDonation(BookApplyDonation bookApplyDonation){
        this.bookApplyDonations.add(bookApplyDonation);
    }

    public void update(String passwordE){
        this.password=passwordE;
    }

}
