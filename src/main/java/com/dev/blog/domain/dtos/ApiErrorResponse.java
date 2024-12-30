package com.dev.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiErrorResponse {

    private int status;
    private String message;
    private List<FieldError> errors;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
