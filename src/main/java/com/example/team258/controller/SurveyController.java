package com.example.team258.controller;

import com.example.team258.dto.SurveyRequestDto;
import com.example.team258.dto.SurveyResponseDto;
import com.example.team258.entity.MessageDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;


    @PostMapping
    public ResponseEntity<MessageDto> createSurvey(@RequestBody SurveyRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(surveyService.createSurvey(requestDto, userDetails.getUser()));
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyResponseDto> getSurvey(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getSurvey(surveyId));
    }

    @GetMapping
    public ResponseEntity<List<SurveyResponseDto>> getAllSurvey() {
        return ResponseEntity.ok(surveyService.getAllSurvey());
    }

    @PutMapping("/{surveyId}")
    public ResponseEntity<MessageDto> updateSurvey(@PathVariable Long surveyId,
                                                   @RequestBody SurveyRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(surveyService.updateSurvey(surveyId, requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/{surveyId}")
    public ResponseEntity<MessageDto> deleteSurvey(@PathVariable Long surveyId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(surveyService.deleteSurvey(surveyId, userDetails.getUser()));
    }

}
