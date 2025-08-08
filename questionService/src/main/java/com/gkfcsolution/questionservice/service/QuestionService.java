package com.gkfcsolution.questionservice.service;

import com.gkfcsolution.questionservice.model.Question;
import com.gkfcsolution.questionservice.model.QuestionResponse;
import com.gkfcsolution.questionservice.model.QuestionWrapper;
import com.gkfcsolution.questionservice.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2025 at 11:25
 * File: null.java
 * Project: quizapp
 *
 * @author Frank GUEKENG
 * @date 06/08/2025
 * @time 11:25
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionRepo questionRepo;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionRepo.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Question>> getAllQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepo.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> addQuestion(Question question) {
        Question savedQuestion = questionRepo.save(question);
        return new ResponseEntity<>("Question added successfully with ID: " + savedQuestion.getId(), HttpStatus.CREATED);
    }

    public String deleteQuestion(int id) {
        if (!questionRepo.existsById(id)) {
            return "Question with ID: " + id + " does not exist.";
        }
        questionRepo.deleteById(id);
        return "Question deleted successfully with ID: " + id;
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, int numberOfQuestions) {
        List<Integer> questions = questionRepo.findRandomQuestionsByCategory(categoryName, numberOfQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Question> questions = questionRepo.findAllById(questionIds);
        if (questions.isEmpty()) {
            return new ResponseEntity<>(questionWrappers, HttpStatus.NOT_FOUND);
        }
        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper(
                    question.getId(),
                    question.getQuestionTitle(),
                    question.getOption1(),
                    question.getOption2(),
                    question.getOption3(),
                    question.getOption4()
            );
            questionWrappers.add(wrapper);
        }
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<QuestionResponse> responses) {
        int score = 0;
        for (QuestionResponse response : responses) {
            Question question = questionRepo.findById(response.getId()).orElse(null);
            if (question != null && response.getResponse().equals(question.getRightAnswer())) {
                score++;
            }
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
