package com.fincons.exception;

public class CourseLessonException extends Exception {

    public CourseLessonException(String s) {
        super(s);
    }

    public static String duplicateException() {
        return "This course has yet this lesson associated";
    }
}
