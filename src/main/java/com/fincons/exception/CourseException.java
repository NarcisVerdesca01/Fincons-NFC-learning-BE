package com.fincons.exception;

public class CourseException extends Exception {
    public CourseException(String s) {
        super(s);
    }

    public static String courseDosNotExist() {
        return "Course does not exist";
    }

    public static String courseAlreadyExist() {
        return "Course already exists";
    }

}
