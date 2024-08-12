package com.devcci.devtoy.member.common.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class BindErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final List<CustomFieldError> errors;

    public BindErrorResponse(List<CustomFieldError> errors) {
        this.errors = initErrors(errors);
    }


    private List<CustomFieldError> initErrors(List<CustomFieldError> errors) {
        return (errors == null) ? new ArrayList<>() : errors;
    }

    @Getter
    public static class CustomFieldError {

        private final String field;
        private final String value;
        private final String reason;

        public CustomFieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }

    public static ResponseEntity<BindErrorResponse> toResponseEntity(HttpStatus status,
                                                                     BindingResult bindingResult) {
        List<CustomFieldError> fieldErrors = getFieldErrors(bindingResult);
        return ResponseEntity
            .status(status)
            .body(new BindErrorResponse(fieldErrors));
    }

    private static List<CustomFieldError> getFieldErrors(
        BindingResult bindingResult) {
        final List<org.springframework.validation.FieldError> errors = bindingResult.getFieldErrors();
        return errors.parallelStream()
            .map(error -> new CustomFieldError(error.getField(),
                error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                error.getDefaultMessage()))
            .toList();
    }


    public static ResponseEntity<BindErrorResponse> toResponseEntity(HttpStatus status,
                                                                     Set<ConstraintViolation<?>> constraintViolations) {
        List<CustomFieldError> list = new ArrayList<>();
        constraintViolations.forEach(error
                -> error.getPropertyPath().spliterator().forEachRemaining(path
                    -> list.add(
                    new CustomFieldError(
                        path.toString(), error.getInvalidValue().toString(), error.getMessage())
                )
            )
        );
        if (!list.isEmpty()) {
            list.remove(0);
        }
        return ResponseEntity
            .status(status)
            .body(new BindErrorResponse(list));
    }
}