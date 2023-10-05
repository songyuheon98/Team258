package com.example.team258.dto;

import com.example.team258.entity.Survey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyResponseDto {
    private String question;
    private String choices;

    public SurveyResponseDto(Survey survey) {
        this.question = survey.getQuestion();
        this.choices = survey.getChoices();
    }
}
