package com.fincons.exception;

public class AbilityException extends Exception {

    public AbilityException(String s) {
        super(s);
    }

    public static String abilityDosNotExist() {
        return "Ability does not exist";
    }

    public static String abilityAlreadyExist() {
        return "Ability already exists";
    }
}
