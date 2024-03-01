package com.fincons.exception;

public class AbilityCourseException extends  Exception {

    public AbilityCourseException(String s) {
        super(s);
    }

    public static String duplicateException() {
        return "This ability has yet this course associate";
    }

}
