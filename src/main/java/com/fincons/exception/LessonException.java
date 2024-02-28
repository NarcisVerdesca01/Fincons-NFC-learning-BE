package com.fincons.exception;

public class LessonException extends Exception {
    public LessonException(String s) {
        super(s);
    }

    public static String lessonDosNotExist() {
        return "Lesson does not exist";
    }

    public static String lessonAlreadyExist() {
        return "Lesson already exists";
    }
}
