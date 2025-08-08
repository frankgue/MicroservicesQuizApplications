package com.gkfcsolution.questionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2025 at 14:33
 * File: null.java
 * Project: quizapp
 *
 * @author Frank GUEKENG
 * @date 06/08/2025
 * @time 14:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private int id;
    private String response;
}
