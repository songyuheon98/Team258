package com.example.team258.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SurveyRequestDto {
    private String question;
    private String choices;
    private Long maxChoice;
    private LocalDateTime deadline;
}
