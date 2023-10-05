package com.example.team258.entity;


import com.example.team258.answer.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
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
    private UserRoleEnum role;

    @OneToMany(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "user")
    private List<Survey> surveys = new ArrayList<>();

    @OneToMany(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "user")
    private List<Answer> answers = new ArrayList<>();

    public void addSurvey(Survey survey) {
        this.surveys.add(survey);
    }
}
