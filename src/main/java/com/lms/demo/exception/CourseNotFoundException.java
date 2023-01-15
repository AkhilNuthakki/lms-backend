package com.lms.demo.exception;

import java.io.Serial;

public class CourseNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CourseNotFoundException(String message) {
        super(message);
    }
}
